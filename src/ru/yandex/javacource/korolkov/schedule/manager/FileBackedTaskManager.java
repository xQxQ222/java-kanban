package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerLoadException;
import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.korolkov.schedule.task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String HEADER = "id,type,name,status,description,epic";
    private final Path path;

    public FileBackedTaskManager(String stringPath) {
        path = Path.of(stringPath);
    }

    private static Task fromString(String value) {
        String[] taskInfo = value.split(",");
        TaskStatus valueStatus = taskInfo[3].equals("NEW") ? TaskStatus.NEW : (taskInfo[3].equals("DONE") ? TaskStatus.DONE : TaskStatus.IN_PROGRESS);
        switch (taskInfo[1]) {
            case "TASK":
                return new Task(Integer.parseInt(taskInfo[0]), taskInfo[2], taskInfo[4], valueStatus);
            case "SUBTASK":
                return new Subtask(Integer.parseInt(taskInfo[0]), taskInfo[2], taskInfo[4], valueStatus, Integer.parseInt(taskInfo[5]));
            case "EPIC":
                return new Epic(Integer.parseInt(taskInfo[0]), taskInfo[2], taskInfo[4], valueStatus);
            default:
                return null;
        }
    }

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file.getPath());
        if (Files.exists(taskManager.path)) {
            List<String> info;
            try {
                info = Files.readAllLines(taskManager.path);
            } catch (IOException ex) {
                throw new ManagerLoadException("Не удалось считать из файла: " + file.getName(), ex);
            }
            int generatorId = 0;
            for (int i = 1; i < info.size(); i++) {
                Task task = fromString(info.get(i));
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                if (task.getType() == TaskTypes.TASK) {
                    taskManager.tasks.put(task.getId(), task);
                } else if (task.getType() == TaskTypes.EPIC) {
                    taskManager.epics.put(task.getId(), (Epic) task);
                } else {
                    Subtask subtask = (Subtask) task;
                    taskManager.subtasks.put(subtask.getId(), subtask);
                }
            }
            for (Subtask subtask : taskManager.subtasks.values()) {
                taskManager.epics.get(subtask.getEpicId()).addSubtask(subtask);
            }
            taskManager.setNewId(generatorId);
        }
        return taskManager;
    }

    private void save() {
        File file = path.toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(HEADER);
            writer.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(toString(task));
                writer.newLine();
            }

            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                final Task task = entry.getValue();
                writer.write(toString(task));
                writer.newLine();
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                final Task task = entry.getValue();
                writer.write(toString(task));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName(), e);
        }
    }

    public static String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," + (task.getType().equals(TaskTypes.SUBTASK) ? ((Subtask) task).getEpicId() : "");
    }

    @Override
    public int addSubtask(Subtask subtask) {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public int addTask(Task task) {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic epic) {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }
}
