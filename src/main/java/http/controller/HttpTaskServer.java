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
import java.util.List;
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

    public void startHttpServer() {
        server.createContext("/tasks/task", this::methodAllocator);
        server.createContext("/tasks/subtask", this::methodAllocator);
        server.createContext("/tasks/epic", this::methodAllocator);
        server.createContext("/tasks", this::methodAllocator);
        server.createContext("/tasks/history", this::methodAllocator);
        server.start();
    }

    /**
     * Какая практика обработки исключений в данном случае будет лучше?
     * Чтобы не прокидывать их выше.
     */
    private void methodAllocator(HttpExchange httpExchange) throws IOException {
        try {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    getTaskAllocator(httpExchange);
                    break;
                case "POST":
                    postAddTaskAllocator(httpExchange);
                    break;
                case "DELETE":
                    deleteTaskAllocator(httpExchange);
                    break;
                case "PUT":
                    putUpdateTaskAllocator(httpExchange);
                    break;
                default:
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_METHOD, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void getTaskAllocator(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String[] split = path.split("/");

            if (split.length == 2) {
                getAllTasks(split[1], httpExchange);
            } else {
                String query = split[2];

                switch (query) {
                    case "task":
                        getTaskById(httpExchange);
                        break;
                    case "epic":
                        getEpicById(httpExchange);
                        break;
                    case "subtask":
                        getSubtaskById(httpExchange);
                        break;
                    case "history":
                        getBrowsingHistory(httpExchange);
                        break;
                    default:
                        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            }
        } finally {
            httpExchange.close();
        }
    }

    private void getAllTasks(String query, HttpExchange httpExchange) throws IOException {
        switch (query) {
            case "tasks":
                getTasks(httpExchange);
                break;
            case "epics":
                getEpics(httpExchange);
                break;
            case "subtasks":
                getSubtasks(httpExchange);
                break;
            default:
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
    }

    private void getTasks(HttpExchange httpExchange) throws IOException {
        try {
            List<Task> epics = managerApp.getTasks();

            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                for (var task : epics) {
                    os.write(gson.toJson(task).getBytes());
                }
            }
        } finally {
            httpExchange.close();
        }
    }

    private void getEpics(HttpExchange httpExchange) throws IOException {
        try {
            List<Epic> tasks = managerApp.getEpics();

            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                for (var epic : tasks) {
                    os.write(gson.toJson(epic).getBytes());
                }
            }
        } finally {
            httpExchange.close();
        }
    }

    private void getSubtasks(HttpExchange httpExchange) throws IOException {
        try {
            List<Subtask> subtasks = managerApp.getSubtasks();

            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                for (var sub : subtasks) {
                    os.write(gson.toJson(sub).getBytes());
                }
            }
        } finally {
            httpExchange.close();
        }
    }

    private void postAddTaskAllocator(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = path.split("/")[2];

            InputStream inputStream = httpExchange.getRequestBody();
            String inputBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            switch (query) {
                case "task":
                    addNewTask(inputBody, httpExchange);
                    break;
                case "epic":
                    addNewEpic(inputBody, httpExchange);
                    break;
                case "subtask":
                    addNewSubtask(inputBody, httpExchange);
                    break;
                default:
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void putUpdateTaskAllocator(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = path.split("/")[2];

            InputStream inputStream = httpExchange.getRequestBody();
            String inputBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            switch (query) {
                case "task":
                    updateTask(inputBody, httpExchange);
                    break;
                case "epic":
                    updateEpic(inputBody, httpExchange);
                    break;
                case "subtask":
                    updateSubtask(inputBody, httpExchange);
                    break;
                default:
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void deleteTaskAllocator(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String query = path.split("/")[2];

            switch (query) {
                case "task":
                    removeTaskById(httpExchange);
                    break;
                case "epic":
                    removeEpicById(httpExchange);
                    break;
                case "subtask":
                    removeSubtaskById(httpExchange);
                    break;
                default:
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void removeTaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = parseId(httpExchange);
            managerApp.removeTaskById(id);

            httpExchange.sendResponseHeaders(200, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void removeEpicById(HttpExchange httpExchange) throws IOException {
        try {
            int id = parseId(httpExchange);
            managerApp.removeEpicById(id);

            httpExchange.sendResponseHeaders(200, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void removeSubtaskById(HttpExchange httpExchange) throws IOException {
        try {
            int id = parseId(httpExchange);
            managerApp.removeSubtaskById(id);

            httpExchange.sendResponseHeaders(200, 0);
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

    private void getBrowsingHistory(HttpExchange httpExchange) throws IOException {
        try {
            String history = managerApp.historyToLine();

            httpExchange.sendResponseHeaders(200, 0);
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(history.getBytes());
            }
        } finally {
            httpExchange.close();
        }
    }

    private void updateTask(String inputBody, HttpExchange httpExchange) throws IOException {
        try {
            Task task = gson.fromJson(inputBody, Task.class);
            managerApp.updateTask(task);

            httpExchange.sendResponseHeaders(200, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void updateEpic(String inputBody, HttpExchange httpExchange) throws IOException {
        try {
            Epic epic = gson.fromJson(inputBody, Epic.class);
            managerApp.updateEpic(epic);

            httpExchange.sendResponseHeaders(200, 0);
        } finally {
            httpExchange.close();
        }
    }

    private void updateSubtask(String inputBody, HttpExchange httpExchange) throws IOException {
        try {
            Subtask subtask = gson.fromJson(inputBody, Subtask.class);
            managerApp.updateSubtask(subtask);

            httpExchange.sendResponseHeaders(200, 0);
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
