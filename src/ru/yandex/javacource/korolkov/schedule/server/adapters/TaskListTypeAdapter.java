package ru.yandex.javacource.korolkov.schedule.server.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.io.IOException;
import java.util.List;

public class TaskListTypeAdapter extends TypeAdapter<List<? extends Task>> {

    @Override
    public void write(JsonWriter jsonWriter, List<? extends Task> tasks) throws IOException {
        jsonWriter.value(tasks.toString());
    }

    @Override
    public List<? extends Task> read(JsonReader jsonReader) throws IOException {
        return List.of();
    }
}
