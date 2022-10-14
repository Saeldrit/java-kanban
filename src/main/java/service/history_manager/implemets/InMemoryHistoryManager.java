package service.history_manager.implemets;

import model.Task;
import service.history_manager.HistoryManager;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList custom;

    public InMemoryHistoryManager() {
        this.custom = new CustomLinkedList();
    }

    @Override
    public void add(Task task) {
        custom.addTask(task);
    }

    @Override
    public List<Task> getHistory() {
        return custom.getTasks();
    }

    @Override
    public void remove(int id) {
        custom.removeTask(id);
    }
}
