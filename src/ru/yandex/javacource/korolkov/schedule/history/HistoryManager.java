package ru.yandex.javacource.korolkov.schedule.history;

import ru.yandex.javacource.korolkov.schedule.manager.Node;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.List;

public interface HistoryManager {
    List<? extends Task> getHistory();

    void add(Task task);

    void removeNode(Node node);
}
