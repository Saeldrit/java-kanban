package service.manager_interface;

import model.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(int id);
}
