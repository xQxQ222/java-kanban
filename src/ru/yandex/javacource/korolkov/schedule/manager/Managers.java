package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.history.HistoryManager;
import ru.yandex.javacource.korolkov.schedule.history.InMemoryHistoryManager;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTaskManager("Records.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
