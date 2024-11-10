package ru.yandex.javacource.korolkov.schedule.task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subtasksIds;

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
        subtasksIds = new ArrayList<>();
    }

    public Epic(String name, String description, ArrayList<Integer> newSubtasksIds) {
        super(name, description, TaskStatus.NEW);
        subtasksIds = newSubtasksIds;
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);

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
        String[] properties = new String[]{String.valueOf(id), String.valueOf(TaskTypes.EPIC), name, String.valueOf(status), description};
        return String.join(";", properties);
    }

    @Override
    public Epic getCopy() {
        return new Epic(this.name, this.description, this.getSubtasksIds());
    }
}
