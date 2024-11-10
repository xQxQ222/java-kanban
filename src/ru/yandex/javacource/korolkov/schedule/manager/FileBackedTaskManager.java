package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.exceptions.ManagerSaveException;
import ru.yandex.javacource.korolkov.schedule.task.Epic;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path path;


    public FileBackedTaskManager(String stringPath) {
        path = Path.of(stringPath);
    }

    static FileBackedTaskManager loadFromFile(File file) throws IOException, NullPointerException {
        FileBackedTaskManager res = new FileBackedTaskManager(file.getPath());
        if (Files.exists(res.path)) {
            List<String> info = Files.readAllLines(res.path);
            for (String item : info) {
                Task task = Task.fromString(item);
                if (task.getClass() == Task.class) {
                    res.tasks.put(task.getId(), task);
                } else if (task.getClass() == Epic.class) {
                    res.epics.put(task.getId(), (Epic) task);
                } else {
                    res.subtasks.put(task.getId(), (Subtask) task);
                }
            }
        }
        return res;
    }

    private void save() throws IOException, ManagerSaveException, NullPointerException {
        try (FileWriter writer = new FileWriter(path.toFile())) {
            for (var item : tasks.values()) {
                String itemInfo = item.toString();
                if (itemInfo.isEmpty() || itemInfo.split(";").length < 5) {
                    throw new ManagerSaveException("Произошла ошибка сохранения записи в файл");
                }
                writer.write(itemInfo + "\n");
            }
            for (var item : epics.values()) {
                String itemInfo = item.toString();
                if (itemInfo.isEmpty() || itemInfo.split(";").length < 5) {
                    throw new ManagerSaveException("Произошла ошибка сохранения записи в файл");
                }
                writer.write(itemInfo + "\n");
            }
            for (Subtask item : subtasks.values()) {
                String itemInfo = item.toString();
                if (itemInfo.isEmpty() || itemInfo.split(";").length < 5 || itemInfo.split(",").length > 6) {
                    throw new ManagerSaveException("Произошла ошибка сохранения записи в файл");
                }
                writer.write(itemInfo + "\n");
            }
        } catch (IOException ex) {
            throw new IOException("Ошибка работы с файлом");
        } catch (ManagerSaveException ex) {
            throw new ManagerSaveException("Ошибка сохранения записи");
        } catch (NullPointerException ex) {
            throw new NullPointerException("Был передан null");
        }
    }

    @Override
    public int addSubtask(Subtask subtask) throws IOException, ManagerSaveException {
        int id = super.addSubtask(subtask);
        save();
        return id;
    }

    @Override
    public int addTask(Task task) throws IOException, ManagerSaveException {
        int id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public int addEpic(Epic epic) throws IOException, ManagerSaveException {
        int id = super.addEpic(epic);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) throws IOException, ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IOException, ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) throws IOException, ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllTasks() throws IOException, ManagerSaveException {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() throws IOException, ManagerSaveException {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() throws IOException, ManagerSaveException {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteTaskById(int taskId) throws IOException, ManagerSaveException {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) throws IOException, ManagerSaveException {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) throws IOException, ManagerSaveException {
        super.deleteEpicById(epicId);
        save();
    }
}
