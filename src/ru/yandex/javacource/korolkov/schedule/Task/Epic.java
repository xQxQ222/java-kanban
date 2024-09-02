package ru.yandex.javacource.korolkov.schedule.Task;

import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;

import java.util.ArrayList;

public class Epic extends Task {

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    private ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public Epic(String name, String description, ArrayList<Integer> newSubtasksIds) {
        super(name, description, TaskStatus.NEW);

        subtasksIds = newSubtasksIds;
    }

    public void addSubtask(Subtask subtask) {
        subtasksIds.add(subtask.getId());
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", subtasksIds=" + subtasksIds +
                "}";
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }
}
