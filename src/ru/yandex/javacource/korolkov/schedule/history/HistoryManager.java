package ru.yandex.javacource.korolkov.schedule.history;

import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.List;

public interface HistoryManager {
    List<? extends Task> getHistory();

    void add(Task task);

    void remove(int id);
}
