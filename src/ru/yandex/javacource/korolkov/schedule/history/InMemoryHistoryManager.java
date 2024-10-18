package ru.yandex.javacource.korolkov.schedule.history;

import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history;
    public Node tail;
    public Node head;


    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> result = new ArrayList<>();
        Node node = head;
        while (node != null) {
            result.add(node.value);
            node = node.next;
        }
        return result;
    }

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        remove(taskId);
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        final Node node = history.remove(id);
        if (node == null) {
            return;
        }
        removeNode(node);
    }

    private void removeNode(Node node) {
        if (node.equals(head)) {
            head = node.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
        } else if (node.equals(tail)) {
            tail = node.prev;
            tail.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    private void linkLast(Task task) {
        Node node = new Node(task);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
        history.put(task.getId(), node);
    }

    private class Node {
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

}
