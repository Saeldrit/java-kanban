package factory;

import repository.HandlerOfInformationInFile;
import repository.composer.ReaderAndWriterHandler;
import service.FileBackedTasksManager;
import service.InMemoryTaskManager;
import service.history_manager.InMemoryHistoryManager;
import service.manager_interface.HistoryManager;
import service.manager_interface.task_manager.ManagerApp;

public class Managers {
    public static ManagerApp getDefault() {
        return new InMemoryTaskManager();
    }

    public static ManagerApp getFileBacked(String path) {
        return new FileBackedTasksManager(path);
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static ReaderAndWriterHandler getHandler() {
        return new HandlerOfInformationInFile();
    }
}
