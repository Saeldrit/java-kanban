package factory;

import service.history_manager.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.manager_interface.HistoryManager;
import service.manager_interface.task_manager.ManagerApp;

public class Managers {
    public static ManagerApp getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
