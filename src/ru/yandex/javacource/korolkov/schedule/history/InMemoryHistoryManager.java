package ru.yandex.javacource.korolkov.schedule.history;

import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> historyViewedElements;

    public InMemoryHistoryManager() {
        historyViewedElements = new ArrayList<>();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyViewedElements;
    }

    @Override
    public void add(Task task) {
        if (historyViewedElements.size() == 10) {
            historyViewedElements.removeFirst();
        }
        historyViewedElements.add(task);
    }
}
