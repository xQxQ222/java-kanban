package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.history.HistoryManager;
import ru.yandex.javacource.korolkov.schedule.history.InMemoryHistoryManager;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public final class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private HistoryManager historyGetterWriter;

    private int newId = 0;

    public InMemoryTaskManager() {
        historyGetterWriter = new InMemoryHistoryManager();
    }

    @Override
    public ArrayList<? extends Task> getHistory() {
        return historyGetterWriter.getHistory();
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksIds();
            updateEpic(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteTaskById(int taskId) {
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
    }

    @Override
    public void deleteEpicById(int epicId) {
        ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        while (!subtasksIds.isEmpty()) {
            deleteSubtaskById(subtasksIds.getFirst());
        }
        epics.remove(epicId);
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task=tasks.get(taskId);
        historyGetterWriter.add(task.getCopy());
        return task;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask=subtasks.get(subtaskId);
        historyGetterWriter.add(subtask.getCopy());
        return subtask;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic=epics.get(epicId);
        historyGetterWriter.add(epic.getCopy());
        return epic;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public int addTask(Task task) {
        if (task.getId() == -1) {
            int generatedId = generateId();
            task.setId(generatedId);
        }
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        int generatedId = -2;
        if (epic != null) {
            if (subtask.getId() == -1) {
                generatedId = generateId();
                subtasks.put(generatedId, subtask);
                subtask.setId(generatedId);
            }
            epic.addSubtask(subtask);
            updateEpicStatus(epic);
        }
        return subtask.getId();
    }

    @Override
    public int addEpic(Epic epic) {
        int generatedId = 0;
        if (epic.getId() == -1) {
            generatedId = generateId();
            epics.put(generatedId, epic);
            epic.setId(generatedId);
        }
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
        updateEpicStatus(epics.get(subtask.getEpicId()));
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
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> tasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        for (int id : epic.getSubtasksIds()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }

    private int generateId() {
        int generatedId = ++newId;
        while (tasks.containsKey(generatedId) || subtasks.containsKey(generatedId) || epics.containsKey(generatedId)) {
            generatedId++;
        }
        return generatedId;
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtasks = getEpicSubtasks(epic.getId());
        ArrayList<TaskStatus> statuses = getSubtasksStatuses(epicSubtasks);
        if (statuses.isEmpty() || !(statuses.contains(TaskStatus.DONE) || statuses.contains(TaskStatus.IN_PROGRESS))) {
            epic.setStatus(TaskStatus.NEW);
        } else if (!statuses.contains(TaskStatus.NEW) && !statuses.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private ArrayList<TaskStatus> getSubtasksStatuses(ArrayList<Subtask> subtasks) {
        ArrayList<TaskStatus> statuses = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            statuses.add(subtask.getTaskStatus());
        }
        return statuses;
    }


}
