package ru.yandex.javacource.korolkov.schedule.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    TaskManager manager;

    @BeforeEach
    public void initializeManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void isHistorySaveAnything(){
        Task task = new Task("Тест","Тест", TaskStatus.NEW);
        assertEquals(0,manager.getHistory().size());
        manager.getTaskById(1);
        assertEquals(1,manager.getHistory().size());
    }

    @Test
    public void isHistorySavePreviousTaskVersions(){
        Task task = new Task("Тест","Тест", TaskStatus.NEW);
        manager.addTask(task);
        manager.getTaskById(1);
        task.setName("Поменяно");
        task.setStatus(TaskStatus.DONE);
        manager.updateTask(task);
        manager.getTaskById(1);
        ArrayList<? extends Task> history=manager.getHistory();
        assertEquals(TaskStatus.NEW,history.get(0).getTaskStatus());
        assertEquals(TaskStatus.DONE,history.get(1).getTaskStatus());
        assertEquals("Поменяно",history.get(1).getName());
        assertEquals("Тест",history.get(0).getName());
    }

    @Test
    public void testHistoryLimit(){
        Task task=new Task("Тест","Тест",TaskStatus.NEW);
        manager.addTask(task);
        for(int i=0;i<12;i++){
            manager.getTaskById(1);
        }
        assertEquals(10,manager.getHistory().size());
    }
}
