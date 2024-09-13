package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.history.HistoryManager;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    public ArrayList<? extends Task> getHistory();

    public void deleteAllTasks();

    public void deleteAllSubtasks();

    public void deleteAllEpics();

    public void deleteTaskById(int taskId);

    public void deleteSubtaskById(int subtaskId);

    public void deleteEpicById(int epicId);

    public Task getTaskById(int taskId);

    public Subtask getSubtaskById(int subtaskId);

    public Epic getEpicById(int epicId);

    public ArrayList<Task> getAllTasks();

    public ArrayList<Subtask> getAllSubtasks();

    public ArrayList<Epic> getAllEpics();

    public int addTask(Task task);

    public int addSubtask(Subtask subtask);

    public int addEpic(Epic epic);

    public void updateTask(Task task);

    public void updateSubtask(Subtask subtask);

    public void updateEpic(Epic epic);

    public ArrayList<Subtask> getEpicSubtasks(int epicId);

}
