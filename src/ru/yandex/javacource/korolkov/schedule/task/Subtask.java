package ru.yandex.javacource.korolkov.schedule.task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        if (epicId != this.id) {
            this.epicId = epicId;
        }
    }

    @Override
    public String toString() {
        String[] properties = new String[]{String.valueOf(id), String.valueOf(TaskTypes.SUBTASK), name, String.valueOf(status), description, String.valueOf(epicId)};
        return String.join(";", properties);
    }

    @Override
    public Subtask getCopy() {
        return new Subtask(this.name, this.description, this.status, this.epicId);
    }
}
