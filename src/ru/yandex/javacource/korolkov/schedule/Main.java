package ru.yandex.javacource.korolkov.schedule;

import ru.yandex.javacource.korolkov.schedule.Task.Epic;
import ru.yandex.javacource.korolkov.schedule.Task.Subtask;
import ru.yandex.javacource.korolkov.schedule.Task.TaskStatus;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Epic epic = new Epic("Сделать уборку", "Нужно сделать полную уборку");
        manager.addEpic(epic);
        System.out.println(manager.getAllTypesOfTasks());
        Subtask subtask1 = new Subtask("Пропылесосить пол", "Взять пылесос и пропылесосить пол", TaskStatus.DONE, 1);
        manager.addSubtask(subtask1);
        System.out.println(manager.getAllTypesOfTasks());
        Subtask subtask2 = new Subtask("Помыть посуду", "Помыть с губкой и мылом", TaskStatus.IN_PROGRESS, 1);
        manager.addSubtask(subtask2);
        System.out.println(manager.getAllTypesOfTasks());
        subtask1.setTaskStatus(TaskStatus.NEW);
        subtask2.setTaskStatus(TaskStatus.NEW);
        manager.updateTask(subtask1);
        manager.updateTask(subtask2);
        System.out.println(manager.getAllTypesOfTasks());
        System.out.println(manager.getSubtaskByEpicId(1));
        System.out.println(manager.getTaskById(245));
        System.out.println(manager.getSubtaskById(2));
        System.out.println(manager.getAllTypesOfTasks());
        manager.deleteEpicById(1);
        System.out.println(manager.getAllTypesOfTasks());
        manager.addEpic(epic);
        manager.deleteAllTasks();
        System.out.println(manager.getAllTypesOfTasks());
    }
}
