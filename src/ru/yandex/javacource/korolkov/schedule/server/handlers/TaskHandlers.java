package ru.yandex.javacource.korolkov.schedule.server.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface TaskHandlers {
    void getJson(HttpExchange exchange, String url) throws IOException;

    void create(HttpExchange exchange, String url) throws IOException;

    void delete(HttpExchange exchange, String url) throws IOException;
}
