package repository.composer;

import service.manager_interface.HistoryManager;

import java.util.List;

public interface Builder {
    void createTasks(List<String> content);

    boolean lookForTask(int id);

    String historyToString(HistoryManager manager);
}
