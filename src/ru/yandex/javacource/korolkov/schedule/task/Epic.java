package ru.yandex.javacource.korolkov.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private LocalDateTime endTime = LocalDateTime.of(0, 1, 1, 0, 0);

    private ArrayList<Integer> subtasksIds;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW, Duration.ofSeconds(0), LocalDateTime.now());
        subtasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, ArrayList<Integer> newSubtasksIds) {
        super(name, description, TaskStatus.NEW, Duration.ofSeconds(0), LocalDateTime.now());
        subtasksIds = newSubtasksIds;
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status, Duration.ofSeconds(0), LocalDateTime.now());
        subtasksIds = new ArrayList<>();
    }

    @Override
    public TaskTypes getType() {
        return TaskTypes.EPIC;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    public void addSubtask(Subtask subtask) {
        subtasksIds.add(subtask.getId());
    }

    public void clearSubtasksIds() {
        subtasksIds.clear();
    }

    public void deleteSubtaskFromEpic(int subtaskId) {
        subtasksIds.remove((Object) subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtasksIds=" + subtasksIds +
                ", duration = " + duration.toMinutes() +
                ", startTime = " + startTime.format(FORMATTER) +
                ", endTime = " + getEndTime().format(FORMATTER) +
                "}";
    }

    @Override
    public Epic getCopy() {
        return new Epic(this.name, this.description, this.getSubtasksIds());
    }
}
