package ru.yandex.javacource.korolkov.schedule.exceptions;

public class CrossedTasksException extends RuntimeException {
    public CrossedTasksException() {
    }

    public CrossedTasksException(String message) {
        super(message);
    }

    public CrossedTasksException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrossedTasksException(Throwable cause) {
        super(cause);
    }
}
