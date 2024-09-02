package ru.yandex.javacource.korolkov.schedule.Task;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, TaskStatus Status, int epicId) {
        super(name, description, Status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
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

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
