import http.controller.HttpTaskServer;
import service.HttpTaskManager;
import service.manager_interface.task_manager.ManagerApp;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ManagerApp httpTaskManager = new HttpTaskManager("http://localhost:8078/");
        HttpTaskServer httpTaskServer = new HttpTaskServer(httpTaskManager);
        httpTaskServer.startServer();
    }
}
