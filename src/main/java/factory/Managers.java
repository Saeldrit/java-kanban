package factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.json_type_adapter.LocalDateTimeAdapter;
import repository.HandlerOfInformationInFile;
import repository.composer.ReaderAndWriterHandler;
import service.HttpTaskManager;
import service.history_manager.HistoryManager;
import service.history_manager.implemets.InMemoryHistoryManager;
import service.manager_interface.task_manager.ManagerApp;

import java.io.IOException;

public class Managers {
    public static ManagerApp getDefault(String urlToServer) {
        return new HttpTaskManager(urlToServer);
    }

    public static HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static ReaderAndWriterHandler getHandler() {
        return new HandlerOfInformationInFile();
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTimeAdapter.class, new LocalDateTimeAdapter())
                .create();
    }
}
