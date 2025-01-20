package ru.yandex.javacource.korolkov.schedule.server.handlers;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.korolkov.schedule.exceptions.CrossedTasksException;
import ru.yandex.javacource.korolkov.schedule.exceptions.NotFoundException;
import ru.yandex.javacource.korolkov.schedule.exceptions.NullTaskException;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler, TaskHandlers {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String url = exchange.getRequestURI().getPath();
        switch (method) {
            case "GET":
                getJson(exchange, url);
                break;
            case "POST":
                create(exchange, url);
                break;
            case "DELETE":
                delete(exchange, url);
                break;
            default:
                sendNotFound(exchange);
        }
    }

    @Override
    public void getJson(HttpExchange exchange, String path) throws IOException {
        if (path.endsWith("tasks")) {
            String json = gson.toJson(manager.getAllTasks());
            sendText(exchange, json);
        } else if (path.split("/").length == 3) {
            int id = Integer.parseInt(path.split("/")[2]);
            try {
                Task task = manager.getTaskById(id);
                String taskJson = gson.toJson(task);
                sendText(exchange, taskJson);
            } catch (NotFoundException ex) {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    public void create(HttpExchange exchange, String url) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task taskFromJson = gson.fromJson(body, Task.class);
        if (url.endsWith("tasks")) {
            try {
                manager.addTask(taskFromJson);
            } catch (NullTaskException ex) {
                sendHasInteractions(exchange, "Значение null недопустимо");
                return;
            } catch (CrossedTasksException ex) {
                sendHasInteractions(exchange, "Добавляемый элемент пересекается с другими");
                return;
            }
            exchange.sendResponseHeaders(201, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Задача успешно создана".getBytes(StandardCharsets.UTF_8));
            }
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            taskFromJson.setId(id);
            try {

                manager.updateTask(taskFromJson);
            } catch (NotFoundException ex) {
                sendNotFound(exchange);
                return;
            } catch (CrossedTasksException ex) {
                sendHasInteractions(exchange, "Добавляемый элемент пересекается с другими");
                return;
            }
            exchange.sendResponseHeaders(201, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Задача успешно обновлена".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    public void delete(HttpExchange exchange, String url) throws IOException {
        if (url.endsWith("tasks")) {
            manager.deleteAllTasks();
            sendText(exchange, "Все задачи успешно удалены");
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            try {
                manager.getTaskById(id);
            } catch (NotFoundException ex) {
                sendNotFound(exchange);
                return;
            }
            manager.deleteTaskById(id);
            sendText(exchange, "Задача с id=" + id + " успешно удалена");
        } else {
            sendNotFound(exchange);
        }
    }
}
