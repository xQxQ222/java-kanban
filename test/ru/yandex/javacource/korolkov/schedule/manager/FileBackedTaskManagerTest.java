package ru.yandex.javacource.korolkov.schedule.manager;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {


    @Test
    public void testEmptyFile() throws IOException {
        Path f = Files.createTempFile("temp", ".txt");
        FileBackedTaskManager emptyTest = FileBackedTaskManager.loadFromFile(f.toFile());
        assertEquals(0, emptyTest.getAllTasks().size());
        assertEquals(0, emptyTest.getAllSubtasks().size());
        assertEquals(0, emptyTest.getAllEpics().size());
    }

    @Test
    public void testReadingFromFile() {
        Path f = Path.of("test.csv");
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(f.toFile());
        assertEquals(2, manager.getAllTasks().size());
        assertEquals(1, manager.getAllEpics().size());
        assertEquals(2, manager.getAllSubtasks().size());
        Task task = new Task("adf", "sdadss", TaskStatus.NEW, Duration.ofMinutes(123), LocalDateTime.now());
        manager.addTask(task);
    }

    @Test
    public void testSavingTasks() throws IOException {
        Path f = Files.createTempFile("temp2", ".txt");
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(f.toFile());
        assertEquals(0, manager.getAllTasks().size());
        Task testTask = new Task(1, "Test task", "ajksfhkj", TaskStatus.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        manager.addTask(testTask);
        assertEquals(1, manager.getAllTasks().size());
        FileBackedTaskManager secondManager = FileBackedTaskManager.loadFromFile(f.toFile());
        assertEquals(1, manager.getAllTasks().size());
    }
}
