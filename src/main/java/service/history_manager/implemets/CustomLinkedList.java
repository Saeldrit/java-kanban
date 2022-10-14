package service.history_manager.implemets;

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
        removeTask(task.getId());
        linkLast(task);
        nodeMap.put(task.getId(), tail);
    }

    public void removeTask(int id) {
        if (nodeMap.containsKey(id)) {
            removeNode(id);
            nodeMap.remove(id);
        }
    }

    private void removeNode(int id) {
        Node node = nodeMap.get(id);
        Node previous = node.previous;
        Node next = node.next;

        if (previous == null) {
            head = next;
        } else {
            previous.next = next;
            node.previous = null;
        }

        if (next == null) {
            tail = previous;
        } else {
            next.previous = previous;
            node.next = null;
        }
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

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node node = head;

        while (node != null) {
            tasks.add(node.task);
            node = node.next;
        }

        return tasks;
    }

    private static class Node {
        private final Task task;
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
