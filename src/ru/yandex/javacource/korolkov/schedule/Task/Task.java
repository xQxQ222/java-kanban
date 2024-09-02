package ru.yandex.javacource.korolkov.schedule.Task;

import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;


    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.status = taskStatus;
    }


    public String getName() {
        return name;
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

    public void setTaskStatus(TaskStatus taskStatus) {
        this.status = taskStatus;
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                "}";
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
}
