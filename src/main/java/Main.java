import factory.Managers;
import http.HttpTaskServer;
import http.server.KVServer;

import java.io.IOException;

/**
 * Привет! Работа сделана не до конца, просто хочу знать, на правильном ли я пути.
 * На данный момент запушил только HttpTaskServer с endpoint.
 */

public class Main {
    public static void main(String[] args) throws IOException {
//        HttpTaskServer httpTaskServer = new HttpTaskServer(Managers.getFileBacked("src/main/resources/tasks.csv"));
//        httpTaskServer.startServer();
        final KVServer kvServer = new KVServer();
        kvServer.start();
    }
}
