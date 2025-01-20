package ru.yandex.javacource.korolkov.schedule.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.korolkov.schedule.exceptions.CrossedTasksException;
import ru.yandex.javacource.korolkov.schedule.exceptions.NotFoundException;
import ru.yandex.javacource.korolkov.schedule.exceptions.NullTaskException;
import ru.yandex.javacource.korolkov.schedule.task.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler, TaskHandlers {
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
    public void getJson(HttpExchange exchange, String url) throws IOException {
        if (url.endsWith("subtasks")) {
            String json = gson.toJson(manager.getAllSubtasks());
            sendText(exchange, json);
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            Subtask subtask = manager.getSubtaskById(id);
            try {
                String taskJson = gson.toJson(subtask);
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
        Subtask subtaskFromJson = gson.fromJson(body, Subtask.class);
        if (url.endsWith("subtasks")) {
            try {
                manager.addSubtask(subtaskFromJson);
            } catch (NullTaskException ex) {
                sendHasInteractions(exchange, "Значение null недопустимо");
                return;
            } catch (CrossedTasksException ex) {
                sendHasInteractions(exchange, "Добавляемый элемент пересекается с другими");
                return;
            }
            exchange.sendResponseHeaders(201, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Сабтаск успешно создан".getBytes(StandardCharsets.UTF_8));
            }
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            try {
                subtaskFromJson.setId(id);
                manager.updateSubtask(subtaskFromJson);
            } catch (NotFoundException ex) {
                sendNotFound(exchange);
                return;
            } catch (CrossedTasksException ex) {
                sendHasInteractions(exchange, "Добавляемый элемент пересекается с другими");
                return;
            }
            exchange.sendResponseHeaders(201, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Сабтаск успешно обновлен".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    public void delete(HttpExchange exchange, String url) throws IOException {
        if (url.endsWith("subtasks")) {
            manager.deleteAllSubtasks();
            sendText(exchange, "Все сабтаски успешно удалены");
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            try {
                manager.getSubtaskById(id);
            } catch (NotFoundException ex) {
                sendNotFound(exchange);
                return;
            }
            manager.deleteSubtaskById(id);
            sendText(exchange, "Сабтаск с id=" + id + " успешно удален");
        } else {
            sendNotFound(exchange);
        }
    }
}
