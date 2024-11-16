package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {

    List<? extends Task> getHistory();

    void deleteAllTasks() throws IOException, ManagerSaveException;

    void deleteAllSubtasks() throws IOException, ManagerSaveException;

    void deleteAllEpics() throws IOException, ManagerSaveException;

    void deleteTaskById(int taskId) throws IOException, ManagerSaveException;

    void deleteSubtaskById(int subtaskId) throws IOException, ManagerSaveException;

    void deleteEpicById(int epicId) throws IOException, ManagerSaveException;

    Task getTaskById(int taskId);

    Subtask getSubtaskById(int subtaskId);

    Epic getEpicById(int epicId);

    List<Task> getAllTasks();

    List<Subtask> getAllSubtasks();

    List<Epic> getAllEpics();

    int addTask(Task task) throws IOException, ManagerSaveException;

    int addSubtask(Subtask subtask) throws IOException, ManagerSaveException;

    int addEpic(Epic epic) throws IOException, ManagerSaveException;

    void updateTask(Task task) throws IOException, ManagerSaveException;

    void updateSubtask(Subtask subtask) throws IOException, ManagerSaveException;

    void updateEpic(Epic epic) throws IOException, ManagerSaveException;

    List<Subtask> getEpicSubtasks(int epicId);

}
