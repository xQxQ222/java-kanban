package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.List;

public interface TaskManager {

    List<? extends Task> getHistory();

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();

    void deleteTaskById(int taskId);

    void deleteSubtaskById(int subtaskId);

    void deleteEpicById(int epicId);

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

}
