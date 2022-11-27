package service.manager_interface.task_manager;

import factory.Managers;
import model.Task;
import service.history_manager.HistoryManager;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ManagerApp implements ManagerTask, ManagerSubtask, ManagerEpic {
    protected final HistoryManager historyManager;

    protected ManagerApp() {
        this.historyManager = Managers.getHistoryManager();
    }

    public List<Task> history() {
        return historyManager.getHistory();
    }

    public String historyToLine() {
        return "\n"
                + historyManager.getHistory()
                .stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }
}
