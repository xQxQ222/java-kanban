package ru.yandex.javacource.korolkov.schedule;

import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Epic epic = new Epic("Сделать уборку", "Нужно сделать полную уборку");
        manager.addEpic(epic);
        System.out.println(manager.getAllEpics());
        Subtask subtask1 = new Subtask("Пропылесосить пол", "Взять пылесос и пропылесосить пол", TaskStatus.DONE, 1);
        manager.addSubtask(subtask1);
        System.out.println(manager.getAllEpics());
        Subtask subtask2 = new Subtask("Помыть посуду", "Помыть с губкой и мылом", TaskStatus.IN_PROGRESS, 1);
        manager.addSubtask(subtask2);
        System.out.println(manager.getAllEpics());
        subtask1.setStatus(TaskStatus.NEW);
        subtask2.setStatus(TaskStatus.NEW);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getEpicSubtasks(1));
        System.out.println(manager.getTaskById(245));
        System.out.println(manager.getSubtaskById(2));
        System.out.println(manager.getAllEpics());
        manager.deleteEpicById(1);
        System.out.println(manager.getAllEpics());
        manager.addEpic(epic);
        manager.deleteAllTasks();
        System.out.println(manager.getAllEpics());
    }
}
