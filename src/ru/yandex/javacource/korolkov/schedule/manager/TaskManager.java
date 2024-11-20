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

    int addTask(Task task);

    int addSubtask(Subtask subtask);

    int addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    List<Subtask> getEpicSubtasks(int epicId);

    List<Task> getPrioritizedTasks();

}
