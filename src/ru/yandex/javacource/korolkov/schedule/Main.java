package ru.yandex.javacource.korolkov.schedule;

import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException, ManagerSaveException {
        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("Помыть пол", "Взять швабру и помыть пол", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        manager.addEpic(new Epic("Сделать дз", "Сделать дз по всем предметам на завтра"));
        manager.addSubtask(new Subtask("Сделать русский язык", "Упражнение №1", TaskStatus.IN_PROGRESS, 2, Duration.ofMinutes(45), LocalDateTime.now().minusMinutes(100)));
        manager.addSubtask(new Subtask("asdf", "sadff", TaskStatus.IN_PROGRESS, 2, Duration.ofMinutes(40), LocalDateTime.now().minusMinutes(141)));
        manager.addTask(new Task("Помыть пол", "Взять швабру и помыть пол", TaskStatus.NEW, Duration.ofMinutes(30), LocalDateTime.now()));
        manager.getTaskById(1);
        manager.getSubtaskById(3);
        System.out.println(manager.getHistory());
        manager.getHistory();
        var f = manager.getPrioritizedTasks();
    }
}
