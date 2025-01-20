package ru.yandex.javacource.korolkov.schedule.http;

import com.google.gson.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.korolkov.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.korolkov.schedule.manager.TaskManager;
import ru.yandex.javacource.korolkov.schedule.server.HttpTaskServer;
import ru.yandex.javacource.korolkov.schedule.server.adapters.DurationTypeAdapter;
import ru.yandex.javacource.korolkov.schedule.server.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.korolkov.schedule.server.adapters.TaskListTypeAdapter;
import ru.yandex.javacource.korolkov.schedule.server.tokens.TaskListTypeToken;
import ru.yandex.javacource.korolkov.schedule.task.Task;
import ru.yandex.javacource.korolkov.schedule.task.TaskStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(new TaskListTypeToken().getType(), new TaskListTypeAdapter())
            .create();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void multiTest() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Test 2", "Testing task 2",
                TaskStatus.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        HttpRequest request1 = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getTasks = client.send(request1, HttpResponse.BodyHandlers.ofString());
        if (getTasks.statusCode() == 200) {
            // передаем парсеру тело ответа в виде строки, содержащей данные в формате JSON
            JsonElement jsonElement = JsonParser.parseString(getTasks.body());

            if (jsonElement.isJsonArray()) {
                JsonArray array = jsonElement.getAsJsonArray();
                int i = array.size();
                assertEquals(1, i);
                JsonObject jsonObject = array.get(0).getAsJsonObject();
                assertEquals("Test 2", jsonObject.get("name").getAsString());
            }
        }
        HttpRequest request2 = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> getDeletedTasks = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getDeletedTasks.statusCode());

    }
}
