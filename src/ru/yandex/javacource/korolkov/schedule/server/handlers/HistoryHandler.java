package ru.yandex.javacource.korolkov.schedule.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.List;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String url = exchange.getRequestURI().getPath();
        if (method.equals("GET") && url.endsWith("history")) {
            List<? extends Task> history = manager.getHistory();
            String json = gson.toJson(history);
            sendText(exchange, json);
        } else {
            sendNotFound(exchange);
        }
    }
}
