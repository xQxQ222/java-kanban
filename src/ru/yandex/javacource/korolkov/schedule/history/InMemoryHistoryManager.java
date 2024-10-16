package ru.yandex.javacource.korolkov.schedule.history;

import ru.yandex.javacource.korolkov.schedule.manager.Node;
import ru.yandex.javacource.korolkov.schedule.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history;
    public Node tail;
    public Node head;

    public InMemoryHistoryManager() {
        history = new LinkedHashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        List<Task> result=new ArrayList<>();
        for(Node node : history.values()){
            result.add(node.value);
        }
        return result;
    }

    @Override
    public void add(Task task) {
        int taskId=task.getId();

        if (history.containsKey(taskId)) {

            removeNode(history.get(taskId));
        }
        Node node = new Node(task);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;

        node.id=taskId;
        history.put(taskId, node);
    }

    @Override
    public void removeNode(Node node) {
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
        history.remove(node.id);
    }

}
