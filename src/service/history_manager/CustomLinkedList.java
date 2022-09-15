package service.history_manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList {

    private final Map<Integer, Node> nodeMap;
    private Node tail;
    private Node head;

    public CustomLinkedList() {
        this.nodeMap = new HashMap<>();
    }

    public void addTask(Task task) {
        nodeMap.remove(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), tail);
    }

    public void removeTask(int id) {
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

    public void linkLast(Task task) {
        Node oldTail = tail;
        Node newNode = new Node(task, tail, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        nodeMap.values().forEach(node -> tasks.add(node.task));

        return tasks;
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
