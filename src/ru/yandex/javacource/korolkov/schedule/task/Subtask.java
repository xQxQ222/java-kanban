package ru.yandex.javacource.korolkov.schedule.task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, TaskStatus Status, int epicId) {
        super(name, description, Status);
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
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                "}";
    }

    @Override
    public Subtask getCopy() {
        return new Subtask(this.name, this.description, this.status, this.epicId);
    }
}
