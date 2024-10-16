package ru.yandex.javacource.korolkov.schedule.manager;

import ru.yandex.javacource.korolkov.schedule.task.Task;

public class Node {
    public int id;
    public Task value;
    public Node prev;
    public Node next;

    public Node(Task value) {
        this.value = value;
        this.next = null;
        this.prev = null;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node task = (Node) o;
        return value.equals(task.value);
    }
}
