package ru.yandex.javacource.korolkov.schedule.task;

import java.util.Objects;

public class Task implements Cloned<Task> {
    protected String name;
    protected String description;
    protected int id;
    protected TaskStatus status;


    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.status = taskStatus;
        this.id = -1;
    }

    public Task(int id, String name, String description, TaskStatus status) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.status = status;
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

    public static Task fromString(String value) {
        String[] taskInfo = value.split(";");
        TaskStatus valueStatus = taskInfo[3].equals("NEW") ? TaskStatus.NEW : (taskInfo[3].equals("DONE") ? TaskStatus.DONE : TaskStatus.IN_PROGRESS);
        switch (taskInfo[1]) {
            case "TASK":
                return new Task(taskInfo[2], taskInfo[4], valueStatus);
            case "SUBTASK":
                return new Subtask(Integer.parseInt(taskInfo[0]), taskInfo[2], taskInfo[4], valueStatus, Integer.parseInt(taskInfo[5]));
            case "EPIC":
                return new Epic(Integer.parseInt(taskInfo[0]), taskInfo[2], taskInfo[4], valueStatus);
            default:
                return null;
        }
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
        String[] properties = new String[]{String.valueOf(id), String.valueOf(TaskTypes.TASK), name, String.valueOf(status), description};
        return String.join(";", properties);
    }


    @Override
    public Task getCopy() {
        return new Task(this.id, this.name, this.description, this.status);
    }
}
