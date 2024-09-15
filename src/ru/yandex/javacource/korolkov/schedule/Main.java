package ru.yandex.javacource.korolkov.schedule;

import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        manager.addTask(new Task("Помыть пол", "Взять швабру и помыть пол", TaskStatus.NEW));
        manager.addEpic(new Epic("Сделать дз", "Сделать дз по всем предметам на завтра"));
        manager.addSubtask(new Subtask("Сделать русский язык", "Упражнения №1,2,3,7", TaskStatus.IN_PROGRESS, 2));
        manager.getTaskById(1);
        manager.getSubtaskById(3);
        System.out.println(manager.getHistory());
        manager.getHistory();
    }
}
