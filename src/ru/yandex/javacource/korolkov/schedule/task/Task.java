package ru.yandex.javacource.korolkov.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Cloned<Task> {
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss");
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(String name, String description, TaskStatus taskStatus, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = taskStatus;
        this.id = -1;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String name, String description, TaskStatus status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public TaskTypes getType() {
        return TaskTypes.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getTaskStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", duration = " + duration.toMinutes() +
                ", startTime = " + startTime.format(FORMATTER) +
                ", endTime = " + getEndTime().format(FORMATTER) +
                "}";
    }


    @Override
    public Task getCopy() {
        return new Task(this.id, this.name, this.description, this.status, this.duration, this.startTime);
    }
}
