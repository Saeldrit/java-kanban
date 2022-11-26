package http.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import factory.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import service.manager_interface.task_manager.ManagerApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final ManagerApp managerApp;
    private final HttpServer server;
    private final Gson gson;

    public HttpTaskServer(ManagerApp managerApp) throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.managerApp = managerApp;
        this.gson = Managers.getGson();
    }

    public void startServer() {
        server.createContext("/tasks/task", this::methodAllocator);
        server.createContext("/tasks/subtask", this::methodAllocator);
        server.createContext("/tasks/epic", this::methodAllocator);
        server.createContext("/tasks/history", this::methodAllocator);
        server.start();
    }

    /**
     *
     * Я думаю тут без объяснений, что запросы DELETE & PUT явно не доделаны)))
     */
    private void methodAllocator(HttpExchange httpExchange) throws IOException {
        try {
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET":
                    getTaskAllocator(httpExchange);
                case "POST":
                    postAddTaskAllocator(httpExchange);
                case "DELETE":
                case "PUT":
                default:
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    /**
     * Какая практика обработки исключений в данном случае будет лучше?
     * Чтобы не прокидывать их выше.
     */
    private void postAddTaskAllocator(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = path.split("/")[2];

            InputStream inputStream = httpExchange.getRequestBody();
            String inputBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            switch (query) {
                case "task":
                    addNewTask(inputBody, httpExchange);
                case "epic":
                    addNewEpic(inputBody, httpExchange);
                case "subtask":
                    addNewSubtask(inputBody, httpExchange);
                default:
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void addNewTask(String inputBody, HttpExchange httpExchange) throws IOException {
        try {
            Task task = gson.fromJson(inputBody, Task.class);
            managerApp.addNewTask(task);

            httpExchange.sendResponseHeaders(200, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void addNewEpic(String inputBody, HttpExchange httpExchange) throws IOException {
        try {
            Epic epic = gson.fromJson(inputBody, Epic.class);
            managerApp.addNewEpic(epic);

            httpExchange.sendResponseHeaders(200, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void addNewSubtask(String inputBody, HttpExchange httpExchange) throws IOException {
        try {
            Subtask subtask = gson.fromJson(inputBody, Subtask.class);
            managerApp.addNewSubtask(subtask);

            httpExchange.sendResponseHeaders(200, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void getTaskAllocator(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = path.split("/")[2];

            switch (query) {
                case "task":
                    getTaskById(httpExchange);
                case "epic":
                    getEpicById(httpExchange);
                case "subtask":
                    getSubtaskById(httpExchange);
                default:
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void getTaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = parseId(httpExchange);

            Optional<Task> optional = Optional.ofNullable(managerApp.getTaskById(id));
            getTaskOrNotFound(optional, httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    private void getEpicById(HttpExchange httpExchange) throws IOException {
        try {
            int id = parseId(httpExchange);

            Optional<Epic> optional = Optional.ofNullable(managerApp.getEpicById(id));
            getTaskOrNotFound(optional, httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    private void getSubtaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = parseId(httpExchange);

            Optional<Subtask> optional = Optional.ofNullable(managerApp.getSubtaskById(id));
            getTaskOrNotFound(optional, httpExchange);
        } finally {
            httpExchange.close();
        }
    }

    private void getTaskOrNotFound(Optional optional, HttpExchange httpExchange) throws IOException {
        if (optional.isPresent()) {
            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(gson.toJson(optional.get()).getBytes());
            }
        } else {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        }
    }

    private int parseId(HttpExchange httpExchange) throws IOException {
        int id = -1;
        try {
            id = Integer.parseInt(
                    httpExchange
                            .getRequestURI()
                            .getRawQuery()
                            .substring("id=".length()));
        } catch (NumberFormatException formatException) {
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        return id;
    }
}
