package ru.yandex.javacource.korolkov.schedule.exceptions;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException() {
    }

    public ManagerLoadException(String message) {
        super(message);
    }

    public ManagerLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerLoadException(Throwable cause) {
        super(cause);
    }

}
