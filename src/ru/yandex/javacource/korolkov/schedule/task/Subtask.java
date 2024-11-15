package ru.yandex.javacource.korolkov.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, TaskStatus status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus status, int epicId, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.SUBTASK;
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
                ", duration = " + duration.toMinutes() +
                ", startTime = " + startTime.format(FORMATTER) +
                ", endTime = " + getEndTime().format(FORMATTER) +
                "}";
    }

    @Override
    public Subtask getCopy() {
        return new Subtask(this.name, this.description, this.status, this.epicId, this.duration, this.startTime);
    }
}
