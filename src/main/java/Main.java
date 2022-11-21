import factory.Managers;
import http.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getFileBacked("src/main/resources/tasks.csv"));
        httpTaskServer.startServer();


    }
}
