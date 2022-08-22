package service.manager_interface;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void add(int taskId);

    List<Task> getHistory();
}
