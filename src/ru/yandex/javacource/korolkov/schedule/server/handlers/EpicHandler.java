package ru.yandex.javacource.korolkov.schedule.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.korolkov.schedule.exceptions.CrossedTasksException;
import ru.yandex.javacource.korolkov.schedule.exceptions.NotFoundException;
import ru.yandex.javacource.korolkov.schedule.exceptions.NullTaskException;
import ru.yandex.javacource.korolkov.schedule.task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler implements HttpHandler, TaskHandlers {

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
        if (url.endsWith("epics")) {
            String json = gson.toJson(manager.getAllEpics());
            sendText(exchange, json);
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            try {
                Epic epic = manager.getEpicById(id);
                String taskJson = gson.toJson(epic);
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
        Epic epicFromJson = gson.fromJson(body, Epic.class);
        if (url.endsWith("epics")) {
            try {
                manager.addEpic(epicFromJson);
            } catch (CrossedTasksException ex) {
                sendHasInteractions(exchange, "Добавляемый элемент пересекается с другими");
                return;
            } catch (NullTaskException ex) {
                sendHasInteractions(exchange, "Значение null недопустимо");
                return;
            }
            exchange.sendResponseHeaders(201, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Эпик успешно создан".getBytes(StandardCharsets.UTF_8));
            }
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            try {
                epicFromJson.setId(id);
                manager.updateEpic(epicFromJson);
            } catch (CrossedTasksException ex) {
                sendHasInteractions(exchange, "Добавляемый элемент пересекается с другими");
                return;
            } catch (NotFoundException ex) {
                sendNotFound(exchange);
                return;
            }
            exchange.sendResponseHeaders(201, 0);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write("Эпик успешно обновлен".getBytes(StandardCharsets.UTF_8));
            }
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    public void delete(HttpExchange exchange, String url) throws IOException {
        if (url.endsWith("epics")) {
            manager.deleteAllEpics();
            sendText(exchange, "Все эпики успешно удалены");
        } else if (url.split("/").length == 3) {
            int id = Integer.parseInt(url.split("/")[2]);
            try {
                manager.getEpicById(id);
            } catch (NotFoundException ex) {
                sendNotFound(exchange);
                return;
            }
            manager.deleteEpicById(id);
            sendText(exchange, "Эпик с id=" + id + " успешно удален");
        } else {
            sendNotFound(exchange);
        }
    }
}
