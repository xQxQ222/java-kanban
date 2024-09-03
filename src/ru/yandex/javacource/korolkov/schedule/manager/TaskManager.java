package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public final class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int newId = 0;

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksIds();
            updateEpic(epic);
        }
        subtasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void deleteSubtaskById(int subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtaskFromEpic(subtaskId);
        updateEpicStatus(epic);
    }

    public void deleteEpicById(int epicId) {
        ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
        while (!subtasksIds.isEmpty()) {
            deleteSubtaskById(subtasksIds.getFirst());
        }
        epics.remove(epicId);
    }

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public int addTask(Task task) {
        int generatedId = generateId();
        task.setId(generatedId);
        tasks.put(generatedId, task);
        return generatedId;
    }

    public int addSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        int generatedId = -1;
        if (epic != null) {
            generatedId = generateId();
            subtasks.put(generatedId, subtask);
            subtask.setId(generatedId);
            epic.addSubtask(subtask);
            updateEpicStatus(epic);
        }
        return generatedId;
    }

    public int addEpic(Epic epic) {
        int generatedId = generateId();
        epics.put(generatedId, epic);
        epic.setId(generatedId);
        return generatedId;
    }

    public void updateTask(Task task) {
        int id = task.getId();
        if (task.getClass() == Task.class) {
            Task oldTask = tasks.get(id);
            if (oldTask != null) {
                tasks.put(id, task);
                task.setId(id);
            }
        }
    }

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

    public void updateEpic(Epic epic) {
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        savedEpic.setName(epic.getName());
        savedEpic.setDescription(epic.getDescription());
    }

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
        return ++newId;
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
