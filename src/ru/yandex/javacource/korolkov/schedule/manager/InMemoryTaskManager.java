package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.korolkov.schedule.history.HistoryManager;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();

    protected final HistoryManager historyGetterWriter;

    protected int newId = 0;

    public InMemoryTaskManager() {
        historyGetterWriter = Managers.getDefaultHistory();
    }

    @Override
    public List<? extends Task> getHistory() {
        return historyGetterWriter.getHistory();
    }

    @Override
    public void deleteAllTasks() throws IOException, ManagerSaveException {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() throws IOException, ManagerSaveException {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksIds();
            updateEpic(epic);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() throws IOException, ManagerSaveException {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void deleteTaskById(int taskId) throws IOException, ManagerSaveException {
        tasks.remove(taskId);
    }

    @Override
    public void deleteSubtaskById(int subtaskId) throws IOException, ManagerSaveException {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtaskFromEpic(subtaskId);
        updateEpicStatus(epic);
    }

    @Override
    public void deleteEpicById(int epicId) throws IOException, ManagerSaveException {
        List<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        while (!subtasksIds.isEmpty()) {
            deleteSubtaskById(subtasksIds.getFirst());
        }
        epics.remove(epicId);
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = tasks.get(taskId);
        historyGetterWriter.add(task.getCopy());
        return task;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        historyGetterWriter.add(subtask.getCopy());
        return subtask;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        historyGetterWriter.add(epic.getCopy());
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
    public int addTask(Task task) throws IOException, ManagerSaveException {
        if (task.getId() == -1) {
            int generatedId = generateId();
            task.setId(generatedId);
        }
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int addSubtask(Subtask subtask) throws IOException, ManagerSaveException {
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
    public int addEpic(Epic epic) throws IOException, ManagerSaveException {
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
    public void updateTask(Task task) throws IOException, ManagerSaveException {
        int id = task.getId();
        Task oldTask = tasks.get(id);
        if (oldTask != null) {
            tasks.put(id, task);
            task.setId(id);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException, ManagerSaveException {
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
    public void updateEpic(Epic epic) throws IOException, ManagerSaveException {
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
        for (int id : epic.getSubtasksIds()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
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
        for (Subtask subtask : subtasks) {
            statuses.add(subtask.getTaskStatus());
        }
        return statuses;
    }


}
