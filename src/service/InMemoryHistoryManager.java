package service;

import model.Task;
import service.manager_interface.HistoryManager;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Set<Task> tasks;
    private int limitSize;

    public InMemoryHistoryManager() {
        tasks = new LinkedHashSet<>();
        limitSize = 10;
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }

    @Override
    public void add(Task task) {
        if (tasks.size() < limitSize) {
            tasks.remove(task);
        } else {
            if (!tasks.remove(task)) {
                tasks.remove(tasks.iterator().next());
            }
        }
        tasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(tasks);
    }
}
