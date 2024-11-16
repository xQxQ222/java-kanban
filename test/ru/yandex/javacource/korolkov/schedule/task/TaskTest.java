package ru.yandex.javacource.korolkov.schedule.task;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTest {

    //экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void checkEqualsOfTwoTaskById() {
        Task taskA = new Task("Помыть посуду", "С мылом", TaskStatus.NEW);
        Task taskB = new Task("Сделать уроки", "Русский язык, математика, чтение", TaskStatus.IN_PROGRESS);
        taskA.setId(1);
        taskB.setId(2);
        assertNotEquals(taskA, taskB);
        taskB.setId(taskA.getId());
        assertEquals(taskA, taskB);
    }

    //наследники класса Task равны друг другу, если равен их id
    @Test
    public void checkEqualsOfTwoHeirsOfTaskById() {
        Epic epic = new Epic("Построить дом", "Построить дом с баней из дерева");
        Subtask subtask = new Subtask("Заложить фундамент", "Замешать цемент, и выложить фундамент", TaskStatus.NEW, epic.getId());
        epic.setId(1);
        subtask.setId(2);
        assertNotEquals(epic, subtask);
        subtask.setId(epic.getId());
        assertNotEquals(epic, subtask);//false, т.к. в переопределенном методе equals класса Task идет сравнение классов. Хоть классы Subtask и Epic наследуют один и тот же класс Task, сами классы друг другу не равны
    }

    //объект Epic нельзя добавить в самого себя в виде подзадачи;
    @Test
    public void isPossibleToPutEpicInEpic() {
        Epic mainEpic = new Epic("Сделать дз", "Сделать все уроки на понедельник");
        Epic subEpic = new Epic("Убраться дома", "Сделать уборку дома");
        //mainEpic.addSubtask(subEpic); Мы не можем добавить epic в epic через методы, ведь требуется параметр типа Subtask
    }

    //объект Subtask нельзя сделать своим же эпиком
    @Test
    public void canSubtaskBeOwnEpic() {
        Subtask subtask = new Subtask("Помыть посуду", "Помыть сковородки, тарелки и т.д", TaskStatus.NEW, 0);
        subtask.setId(1);
        subtask.setEpicId(subtask.getId());
        assertNotEquals(subtask.getId(), subtask.getEpicId());
    }

    //тест автоматического расчета статуса эпика
    @Test
    public void testChangeInEpicStatus() throws IOException, ManagerSaveException {
        TaskManager manager = Managers.getDefault();

        Epic epic = new Epic("Тест", "Тест");
        manager.addEpic(epic);

        assertEquals(TaskStatus.NEW, epic.getStatus());

        Subtask subtaskDone = new Subtask("jh", "dfg", TaskStatus.DONE, 1);
        manager.addSubtask(subtaskDone);

        assertEquals(TaskStatus.DONE, epic.getStatus());

        Subtask subtaskInProgress = new Subtask("df", "ijdkjfi", TaskStatus.IN_PROGRESS, 1);
        manager.addSubtask(subtaskInProgress);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());

        subtaskDone.setStatus(TaskStatus.NEW);
        subtaskInProgress.setStatus(TaskStatus.NEW);
        manager.updateSubtask(subtaskDone);
        manager.updateSubtask(subtaskInProgress);

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }
}
