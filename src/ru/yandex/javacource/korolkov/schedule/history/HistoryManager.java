package ru.yandex.javacource.korolkov.schedule.history;

import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.ArrayList;

public interface HistoryManager {
    public ArrayList<? extends Task> getHistory();

    public void add(Task task);
}
