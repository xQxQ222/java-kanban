package ru.yandex.javacource.korolkov.schedule.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.korolkov.schedule.manager.Managers;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.server.adapters.DurationTypeAdapter;
import ru.yandex.javacource.korolkov.schedule.server.adapters.TaskListTypeAdapter;
import ru.yandex.javacource.korolkov.schedule.server.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.korolkov.schedule.server.tokens.TaskListTypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler {

    protected static final TaskManager manager = Managers.getDefault();
    protected Gson gson;

    public BaseHttpHandler() {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(new TaskListTypeToken().getType(), new TaskListTypeAdapter())
                .create();
    }


    public TaskManager getManager() {
        return manager;
    }

    public void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(resp);
        }
    }

    public void sendNotFound(HttpExchange exchange) throws IOException {
        String phrase = "Произошло обращение к несуществующему элементу";
        exchange.sendResponseHeaders(404, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(phrase.getBytes(StandardCharsets.UTF_8));
        }
    }

    public void sendHasInteractions(HttpExchange exchange, String text) throws IOException {
        exchange.sendResponseHeaders(406, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes(StandardCharsets.UTF_8));
        }
    }
}
