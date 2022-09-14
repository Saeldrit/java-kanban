package service;

import model.Task;
import service.manager_interface.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> nodeMap;
    private Node tail;
    private Node head;

    public InMemoryHistoryManager() {
        this.nodeMap = new LinkedHashMap<>();
    }

    @Override
    public void add(Task task) {
        nodeMap.remove(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        nodeMap.values().forEach(e -> tasks.add(e.task));

        return tasks;
    }

    @Override
    public void remove(int id) {
        removeTask(id);
    }

    private void removeTask(int id) {
        Node oldTailPrev = nodeMap.get(id).previous;

        nodeMap.get(id).task = null;
        nodeMap.get(id).previous = null;
        tail = oldTailPrev;

        if (oldTailPrev == null) {
            head = null;
        } else {
            oldTailPrev.next = null;
        }

        nodeMap.remove(id);
    }

    private void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(task, tail, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    private static class Node {
        private Task task;
        private Node previous;
        private Node next;

        public Node(Task task, Node previous, Node next) {
            this.task = task;
            this.previous = previous;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "task=" + task
                    + ", previous=" + (previous != null ? previous.task : null)
                    + ", next=" + (next != null ? next.task : null)
                    + '}';
        }
    }
}
