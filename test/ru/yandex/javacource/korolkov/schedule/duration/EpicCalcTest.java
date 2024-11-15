package ru.yandex.javacource.korolkov.schedule.duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EpicCalcTest {
    TaskManager manager;

    @BeforeEach
    void bE() {
        manager = Managers.getDefault();
    }

    @Test
    public void testCalcEpicEndTime() {
        Epic epic = new Epic("afss", "safds");
        manager.addEpic(epic);
        Subtask s1 = new Subtask("named", "sdad", TaskStatus.NEW, 1, Duration.ofMinutes(10), LocalDateTime.now());
        assertEquals(LocalDateTime.of(0, 1, 1, 0, 0), epic.getEndTime());
        manager.addSubtask(s1);
        assertEquals(s1.getEndTime(), epic.getEndTime());
        Subtask lateSub = new Subtask("names", "late", TaskStatus.NEW, 1, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(404));
        manager.addSubtask(lateSub);
        assertEquals(lateSub.getEndTime(), epic.getEndTime());
    }

    @Test
    public void testEpicDuration() {
        Epic epic = new Epic("afss", "safds");
        manager.addEpic(epic);
        Subtask s1 = new Subtask("named", "sdad", TaskStatus.NEW, 1, Duration.ofMinutes(10), LocalDateTime.now());
        manager.addSubtask(s1);
        assertEquals(10, epic.getDuration().toMinutes());
        Subtask lateSub = new Subtask("names", "late", TaskStatus.NEW, 1, Duration.ofMinutes(122), LocalDateTime.now().plusMinutes(404));
        manager.addSubtask(lateSub);
        assertEquals(132, epic.getDuration().toMinutes());
        Subtask done = new Subtask("names", "done", TaskStatus.DONE, 1, Duration.ofMinutes(100), LocalDateTime.now().plusMinutes(800));
        manager.addSubtask(done);
        assertEquals(132, epic.getDuration().toMinutes());
    }

}
