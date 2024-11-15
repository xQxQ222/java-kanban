package ru.yandex.javacource.korolkov.schedule.duration;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.exceptions.CrossedTasksException;
import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CrossTests {
    @Test
    public void testTasksTimeCross() {
        TaskManager manager = Managers.getDefault();
        Task t1 = new Task("sdad", "sdadd", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        Task t2 = new Task("sdad", "sdadd", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now().plusMinutes(5));
        manager.addTask(t1);
        assertThrows(CrossedTasksException.class, () -> {
            manager.addTask(t2);
        });
    }
}
