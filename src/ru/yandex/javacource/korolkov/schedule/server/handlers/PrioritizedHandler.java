package ru.yandex.javacource.korolkov.schedule.server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String url = exchange.getRequestURI().getPath();
        if (method.equals("GET") && url.endsWith("prioritized")) {
            List<Task> prioritizedTasks = manager.getPrioritizedTasks();
            String json = gson.toJson(prioritizedTasks);
            sendText(exchange, json);
        } else {
            sendNotFound(exchange);
        }
    }
}
