import factory.Managers;
import http.HttpTaskServer;

public class Main {
    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getFileBacked("src/main/resources/tasks.csv"));
        httpTaskServer.startServer();
    }
}
