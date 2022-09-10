package service;

import model.Task;
import service.manager_interface.HistoryManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Task> tasks;
    private int limitSize;

    public InMemoryHistoryManager() {
        tasks = new LinkedHashMap<>();
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
            tasks.remove(task.getId());
        } else {
            if (tasks.remove(task.getId()) == null) {
                tasks.remove(tasks.keySet().iterator().next());
            }
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void remove(int id) {
        tasks.remove(id);
    }
}
