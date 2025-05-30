package ru.yandex.javacource.korolkov.schedule.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryManagerTest {
    TaskManager manager;

    @BeforeEach
    public void initializeManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void isHistorySaveAnything() throws IOException, ManagerSaveException {
        Task task = new Task("Тест", "Тест", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        manager.addTask(task);
        assertEquals(0, manager.getHistory().size());
        manager.getTaskById(1);
        assertEquals(1, manager.getHistory().size());
    }

    @Test
    public void checkNodeLinks() throws IOException, ManagerSaveException {
        Task task = new Task("Тест", "Тест", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        manager.addTask(task);
        Task task2 = new Task("Тест2", "Тест2", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(20));
        manager.addTask(task2);
        Task task3 = new Task("Тест3", "Тест3", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(40));
        manager.addTask(task3);
        for (int i = 1; i <= 3; i++)
            manager.getTaskById(i);
        List<? extends Task> history = manager.getHistory();
        assertEquals(3, history.size());
        Task lastTask = history.getLast();
        assertEquals(task3, lastTask);

        manager.getTaskById(2);
        List<? extends Task> historyAfterShuffle = manager.getHistory();
        assertEquals(3, historyAfterShuffle.size());
        Task lastTaskAfterShuffle = historyAfterShuffle.getLast();
        assertEquals(lastTaskAfterShuffle, task2);
    }

    @Test
    public void isHistorySaveRepeatedTasks() throws IOException, ManagerSaveException {
        Task task = new Task("Тест", "Тест", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        manager.addTask(task);
        for (int i = 0; i < 10; i++) {
            manager.getTaskById(1);
        }
        var history = manager.getHistory();
        assertEquals(1, history.size());
    }
}
