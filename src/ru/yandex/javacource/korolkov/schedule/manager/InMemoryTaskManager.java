package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.exceptions.CrossedTasksException;
import ru.yandex.javacource.korolkov.schedule.history.HistoryManager;
import ru.yandex.javacource.korolkov.schedule.task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.function.BiPredicate;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected final HistoryManager historyManager;

    protected int newId = 0;

    protected void setNewId(int newId) {
        if (newId < 0) {
            throw new IllegalArgumentException("Счетчик не должен начинаться с числа меньше нуля");
        }
        this.newId = newId;
    }

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<? extends Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void deleteAllTasks() {
        prioritizedTasks.removeIf(task -> task.getType() == TaskTypes.TASK);
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        epics.values().stream()
                .peek(Epic::clearSubtasksIds)
                .forEach(this::updateEpic);
        prioritizedTasks.removeIf(task -> task.getType() == TaskTypes.SUBTASK);
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
        prioritizedTasks.removeIf(task -> task.getType() == TaskTypes.SUBTASK);
        prioritizedTasks.removeIf(task -> task.getType() == TaskTypes.EPIC);
    }

    @Override
    public void deleteTaskById(int taskId) {
        prioritizedTasks.removeIf(task -> task.getId() == taskId);
        tasks.remove(taskId);
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask == null) {
            return;
        }

        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtaskFromEpic(subtaskId);
        updateEpicStatus(epic);
        updateEpicTimeProperties(epic);
        prioritizedTasks.remove(subtask);
    }

    @Override
    public void deleteEpicById(int epicId) {
        List<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        while (!subtasksIds.isEmpty()) {
            deleteSubtaskById(subtasksIds.getFirst());
        }
        prioritizedTasks.remove(epics.get(epicId));
        epics.remove(epicId);
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task.getCopy());
        return task;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        historyManager.add(subtask.getCopy());
        return subtask;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyManager.add(epic.getCopy());
        return epic;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public int addTask(Task task) {
        int generatedId = generateId();
        task.setId(generatedId);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
        canTaskBeAddedToTree(task);
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        int generatedId = -2;
        if (epic != null) {
            generatedId = generateId();
            prioritizedTasks.add(subtask);
            subtasks.put(generatedId, subtask);
            canTaskBeAddedToTree(subtask);
            subtask.setId(generatedId);
            epic.addSubtask(subtask);
            updateEpicStatus(epic);
            updateEpicTimeProperties(epic);
        }
        return subtask.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        int generatedId = 0;
        generatedId = generateId();
        prioritizedTasks.add(epic);
        epics.put(generatedId, epic);
        canTaskBeAddedToTree(epic);
        epic.setId(generatedId);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        Task oldTask = tasks.get(id);
        if (oldTask != null) {
            tasks.put(id, task);
            task.setId(id);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        Subtask oldSubtask = subtasks.get(id);
        if (oldSubtask == null) {
            return;
        }
        final Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtask.setId(id);
        subtasks.put(id, subtask);
        updateEpicStatus(epic);
        updateEpicTimeProperties(epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        List<Subtask> tasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        epic.getSubtasksIds().stream()
                .map(subtasks::get)
                .forEach(tasks::add);
        return tasks;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    protected Optional<LocalDateTime> findEarlierSubtaskTimeOfEpic(int epicId) {
        List<Integer> epicSubtaskIds = epics.get(epicId).getSubtasksIds();
        return subtasks.values().stream()
                .filter(subtask -> epicSubtaskIds.contains(subtask.getId()))
                .filter(subtask -> subtask.getStatus() != TaskStatus.DONE)
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo);
    }

    protected Optional<LocalDateTime> findLatestSubtaskTimeOfEpic(int epicId) {
        List<Integer> epicSubtaskIds = epics.get(epicId).getSubtasksIds();
        return subtasks.values().stream()
                .filter(subtask -> epicSubtaskIds.contains(subtask.getId()))
                .filter(subtask -> subtask.getStatus() != TaskStatus.DONE)
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo);
    }

    protected long getEpicDuration(int epicId) {
        List<Integer> epicSubTaskIds = epics.get(epicId).getSubtasksIds();
        return subtasks.values().stream()
                .filter(subtask -> epicSubTaskIds.contains(subtask.getId()))
                .filter(subtask -> subtask.getStatus() != TaskStatus.DONE)
                .map(Subtask::getDuration)
                .mapToLong(Duration::toMinutes)
                .sum();
    }

    protected void updateEpicTimeProperties(Epic epic) {
        int epicId = epic.getId();
        Optional<LocalDateTime> minDateTime = findEarlierSubtaskTimeOfEpic(epicId);
        minDateTime.ifPresent(epic::setStartTime);
        epic.setDuration(Duration.ofMinutes(getEpicDuration(epicId)));
        Optional<LocalDateTime> maxDateTime = findLatestSubtaskTimeOfEpic(epicId);
        maxDateTime.ifPresent(epic::setEndTime);
    }

    protected int generateId() {
        int generatedId = ++newId;
        while (tasks.containsKey(generatedId) || subtasks.containsKey(generatedId) || epics.containsKey(generatedId)) {
            generatedId++;
        }
        return generatedId;
    }

    protected void updateEpicStatus(Epic epic) {
        List<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());
        List<TaskStatus> statuses = getSubtasksStatuses(epicSubtasks);
        if (statuses.isEmpty() || !(statuses.contains(TaskStatus.DONE) || statuses.contains(TaskStatus.IN_PROGRESS))) {
            epic.setStatus(TaskStatus.NEW);
        } else if (!statuses.contains(TaskStatus.NEW) && !statuses.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    protected List<TaskStatus> getSubtasksStatuses(List<Subtask> subtasks) {
        List<TaskStatus> statuses = new ArrayList<>();
        subtasks.stream()
                .map(Subtask::getTaskStatus)
                .forEach(statuses::add);
        return statuses;
    }

    protected BiPredicate<Task, Task> isTimeCrossed = (t1, t2) -> ((t1.getStartTime().isBefore(t2.getEndTime()) || t1.getStartTime().isEqual(t2.getEndTime())) &&
            (t1.getEndTime().isAfter(t2.getStartTime()) || t1.getEndTime().isEqual(t2.getStartTime())));

    protected void canTaskBeAddedToTree(Task task) {
        boolean checkCross = prioritizedTasks.stream()
                .filter(existedTask -> task != existedTask)
                .anyMatch(existedTask -> isTimeCrossed.test(existedTask, task));

        if (checkCross) {
            throw new CrossedTasksException("При добавлении новой задачи появилось пересечение по времени");
        }
    }
}
