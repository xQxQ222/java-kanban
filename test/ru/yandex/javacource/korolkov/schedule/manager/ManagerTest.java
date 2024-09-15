package ru.yandex.javacource.korolkov.schedule.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import static org.junit.jupiter.api.Assertions.*;

public class ManagerTest {

    TaskManager manager;

    @BeforeEach
    void initializeTaskManager() {
        manager = Managers.getDefault();
    }

    //утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void uClassReturnValidManager() {
        assertNotNull(manager);
        manager.addTask(new Task("Test", "Test", TaskStatus.NEW));
        assertEquals(1, manager.getAllTasks().size());
    }

    //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    @Test
    public void isInMemoryTaskManagerWorkCorrectly() {
        manager.addTask(new Task("Таск", "Таск", TaskStatus.NEW));
        manager.addEpic(new Epic("Epic", "Epic"));
        manager.addSubtask(new Subtask("Subtask", "Subtask", TaskStatus.NEW, 2));
        assertNotSame(manager.getTaskById(1).getClass(), manager.getSubtaskById(3).getClass());
        assertNotSame(manager.getTaskById(1).getClass(), manager.getEpicById(2).getClass());
        assertEquals(Subtask.class, manager.getSubtaskById(3).getClass());
    }

    //задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    public void differentTaskIdGeneratingDidntConflict() {
        TaskManager manager = Managers.getDefault();

        Task task1WithSetId = new Task("Task1", "Task1", TaskStatus.NEW);
        task1WithSetId.setId(2);

        Task task2GeneratedId = new Task("Task2", "Task2", TaskStatus.NEW);

        Task task3WithGeneratedId = new Task("Task3", "Task3", TaskStatus.NEW);

        manager.addTask(task1WithSetId);
        manager.addTask(task2GeneratedId);
        manager.addTask(task3WithGeneratedId);

        assertEquals(2, task1WithSetId.getId());
        assertEquals(1, task2GeneratedId.getId());
        assertEquals(3, task3WithGeneratedId.getId());

    }

    //проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void isTaskImmutableWhenAddedInManager() {
        Task task = new Task("Task", "Task", TaskStatus.NEW);
        task.setId(1);
        manager.addTask(task);
        Task taskFromManager = manager.getTaskById(1);
        assertEquals(task, taskFromManager);
    }


}
