package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.Task.Epic;
import ru.yandex.javacource.korolkov.schedule.Task.Subtask;
import ru.yandex.javacource.korolkov.schedule.Task.Task;
import ru.yandex.javacource.korolkov.schedule.Task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int newId = 0;

    private int generateId() {
        return ++newId;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllTypesOfTasks() {
        deleteAllEpics();
        deleteAllTasks();
    }

    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
    }

    public void deleteSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            subtasks.remove(subtaskId);
        }
    }

    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            ArrayList<Integer> subtasksIds = epics.get(epicId).getSubtasksIds();
            for (int subtaskId : subtasksIds) {
                deleteSubtaskById(subtaskId);
            }
            epics.remove(epicId);
        }
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

    public ArrayList<Task> getAllTypesOfTasks() {
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        allTasks.addAll(subtasks.values());
        allTasks.addAll(epics.values());
        return allTasks;
    }

    public void addTask(Task task) {
        int generatedId = 0;
        if (task.getClass() == Task.class) {
            generatedId = generateId();
            tasks.put(generatedId, task);
            task.setId(generatedId);
        }
    }

    public void addSubtask(Subtask subtask) {
        int generatedId = generateId();
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            subtasks.put(generatedId, subtask);
            subtask.setId(generatedId);
            calculateEpicStatus(epic);
            epic.addSubtask(subtask);
        }
    }

    public void addEpic(Epic epic) {
        int generatedId = generateId();
        epics.put(generatedId, epic);
        epic.setId(generatedId);
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
        if (oldSubtask != null) {
            subtask.setId(id);
            subtasks.put(id, subtask);
            calculateEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void updateEpic(Epic epic) {
        int id = epic.getId();
        Epic oldEpic = epics.get(id);
        if (oldEpic != null) {
            epic.setId(id);
            epics.put(id, epic);
            calculateEpicStatus(epic);
            ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
            for (int sId : subtasksIds) {
                deleteSubtaskById(sId);
            }
        }
    }

    public ArrayList<Subtask> getSubtaskByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
            ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
            for (int subtaskId : subtasksIds) {
                subtasksOfEpic.add(subtasks.get(subtaskId));
            }
            return subtasksOfEpic;
        }
        return null;
    }

    private void calculateEpicStatus(Epic epic) {
        ArrayList<Subtask> epicSubtasks = getSubtaskByEpicId(epic.getId());
        ArrayList<TaskStatus> statuses = getSubtasksStatuses(epicSubtasks);
        if (statuses.isEmpty() || !(statuses.contains(TaskStatus.DONE) || statuses.contains(TaskStatus.IN_PROGRESS))) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (!statuses.contains(TaskStatus.NEW) && !statuses.contains(TaskStatus.IN_PROGRESS)) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
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
