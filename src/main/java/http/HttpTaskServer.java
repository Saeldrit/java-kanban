package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import model.Epic;
import model.Subtask;
import model.Task;
import service.manager_interface.task_manager.ManagerApp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final ManagerApp managerApp;
    private final HttpServer httpServer;
    private final Gson gson;

    @SneakyThrows
    public HttpTaskServer(ManagerApp managerApp) {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.managerApp = managerApp;
        this.gson = new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }

    public void startServer() {
        postTasksAddNew();
        separateRequestMethod();
        httpServer.start();
    }

    public void stopServer() {
        httpServer.start();
    }

    private void postTasksAddNew() {
        httpServer.createContext("/tasks/add-new/task", new AddNewTaskHandler());
        httpServer.createContext("/tasks/add-new/subtask", new AddNewTaskHandler());
        httpServer.createContext("/tasks/add-new/epic", new AddNewTaskHandler());
    }

    private void separateRequestMethod() {
        httpServer.createContext("/tasks/task", new QueryHandler());
        httpServer.createContext("/tasks/epic", new QueryHandler());
        httpServer.createContext("/tasks/subtask", new QueryHandler());
        httpServer.createContext("/tasks/tasks", new QueryHandler());
        httpServer.createContext("/tasks/epics", new QueryHandler());
        httpServer.createContext("/tasks/subtasks", new QueryHandler());
    }

    private class QueryHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            String query = exchange.getRequestMethod();

            switch (query) {
                case "GET":
                    new GetTaskHandler().handle(exchange);
                case "DELETE":
                    new DeleteTaskHandler().handle(exchange);
                case "PUT":
                    new PutUpdateTaskHandler().handle(exchange);
            }
        }
    }

    private class GetTaskHandler {

        public void handle(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();
            String query = path.split("/")[2];
            int id = 0;

            if (path.split("/").length > 3) {
                id = Integer.parseInt(path.split("/")[3]);
            }

            switch (query) {
                case "task":
                    getTaskById(id, exchange);
                case "epic":
                    getEpicById(id, exchange);
                case "subtask":
                    getSubtaskById(id, exchange);
                case "tasks":
                    getTasks(exchange);
                case "epics":
                    getEpics(exchange);
                case "subtasks":
                    getSubtasks(exchange);
            }
        }

        @SneakyThrows
        private void getTasks(HttpExchange exchange) {
            List<Task> taskList = managerApp.getTasks();

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (!taskList.isEmpty()) {
                    for (var task : taskList) {
                        os.write(gson.toJson(task).getBytes());
                    }
                }
            }
        }

        @SneakyThrows
        private void getEpics(HttpExchange exchange) {
            List<Epic> epicList = managerApp.getEpics();

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (!epicList.isEmpty()) {
                    for (var epic : epicList) {
                        os.write(gson.toJson(epic).getBytes());
                    }
                }
            }
        }

        @SneakyThrows
        private void getSubtasks(HttpExchange exchange) {
            List<Subtask> subtaskList = managerApp.getSubtasks();

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (!subtaskList.isEmpty()) {
                    for (var sub : subtaskList) {
                        os.write(gson.toJson(sub).getBytes());
                    }
                }
            }
        }

        @SneakyThrows
        private void getTaskById(int id, HttpExchange exchange) {
            Task task = managerApp.getTaskById(id);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (task != null) {
                    os.write(gson.toJson(task).getBytes());
                }
            }
        }

        @SneakyThrows
        private void getEpicById(int id, HttpExchange exchange) {
            Epic epic = managerApp.getEpicById(id);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (epic != null) {
                    os.write(gson.toJson(epic).getBytes());
                }
            }
        }

        @SneakyThrows
        private void getSubtaskById(int id, HttpExchange exchange) {
            Subtask subtask = managerApp.getSubtaskById(id);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (subtask != null) {
                    os.write(gson.toJson(subtask).getBytes());
                }
            }
        }
    }

    private class DeleteTaskHandler {

        public void handle(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();
            String query = path.split("/")[2];
            int id = 0;

            if (path.split("/").length > 3) {
                id = Integer.parseInt(path.split("/")[3]);
            }

            switch (query) {
                case "task":
                    deleteTaskById(id, exchange);
                case "epic":
                    deleteEpicById(id, exchange);
                case "subtask":
                    deleteSubtaskById(id, exchange);
                case "tasks":
                    deleteTasks(exchange);
                case "epics":
                    deleteEpics(exchange);
                case "subtasks":
                    deleteSubtasks(exchange);
            }
        }

        @SneakyThrows
        private void deleteTaskById(int id, HttpExchange exchange) {
            Task task = managerApp.getTaskById(id);
            managerApp.removeTaskById(id);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (!managerApp.getTasks().contains(task)) {
                    os.write("true".getBytes());
                }
            }
        }

        @SneakyThrows
        private void deleteEpicById(int id, HttpExchange exchange) {
            Epic epic = managerApp.getEpicById(id);
            managerApp.removeEpicById(id);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (!managerApp.getEpics().contains(epic)) {
                    os.write("true".getBytes());
                }
            }
        }

        @SneakyThrows
        private void deleteSubtaskById(int id, HttpExchange exchange) {
            Subtask subtask = managerApp.getSubtaskById(id);
            managerApp.removeTaskById(id);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (!managerApp.getSubtasks().contains(subtask)) {
                    os.write("true".getBytes());
                }
            }
        }

        @SneakyThrows
        private void deleteTasks(HttpExchange exchange) {
            managerApp.removeTasks();

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (managerApp.getTasks().isEmpty()) {
                    os.write("true".getBytes());
                }
            }
        }

        @SneakyThrows
        private void deleteEpics(HttpExchange exchange) {
            managerApp.removeEpics();

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (managerApp.getEpics().isEmpty()) {
                    os.write("true".getBytes());
                }
            }
        }

        @SneakyThrows
        private void deleteSubtasks(HttpExchange exchange) {
            managerApp.removeSubtasks();

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                if (managerApp.getSubtasks().isEmpty()) {
                    os.write("true".getBytes());
                }
            }
        }
    }

    private class PutUpdateTaskHandler {

        @SneakyThrows
        public void handle(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();
            String query = path.split("/")[2];

            InputStream inputStream = exchange.getRequestBody();
            String inputBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            switch (query) {
                case "task":
                    updateTaskById(inputBody, exchange);
                case "epic":
                    updateEpicById(inputBody, exchange);
                case "subtask":
                    updateSubtaskBy(inputBody, exchange);
            }
        }

        @SneakyThrows
        private void updateTaskById(String inputBody, HttpExchange exchange) {
            Task task = gson.fromJson(inputBody, Task.class);
            managerApp.updateTask(task);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson.toJson(task).getBytes());
            }
        }

        @SneakyThrows
        private void updateEpicById(String inputBody, HttpExchange exchange) {
            Epic epic = gson.fromJson(inputBody, Epic.class);
            managerApp.updateEpic(epic);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson.toJson(epic).getBytes());
            }
        }

        @SneakyThrows
        private void updateSubtaskBy(String inputBody, HttpExchange exchange) {
            Subtask subtask = gson.fromJson(inputBody, Subtask.class);
            managerApp.updateSubtask(subtask);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson.toJson(subtask).getBytes());
            }
        }
    }

    private class AddNewTaskHandler implements HttpHandler {

        @SneakyThrows
        @Override
        public void handle(HttpExchange exchange) {
            String path = exchange.getRequestURI().getPath();
            String query = path.split("/")[3];

            InputStream inputStream = exchange.getRequestBody();
            String inputBody = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            switch (query) {
                case "task":
                    addNewTask(inputBody, exchange);
                case "epic":
                    addNewEpic(inputBody, exchange);
                case "subtask":
                    addNewSubtask(inputBody, exchange);
            }
        }

        @SneakyThrows
        private void addNewTask(String inputBody, HttpExchange exchange) {
            Task task = gson.fromJson(inputBody, Task.class);
            managerApp.addNewTask(task);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson
                        .toJson(managerApp
                                .getTaskById(task.getId()))
                        .getBytes());
            }
        }

        @SneakyThrows
        private void addNewSubtask(String inputBody, HttpExchange exchange) {
            Subtask subtask = gson.fromJson(inputBody, Subtask.class);
            managerApp.addNewSubtask(subtask);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson
                        .toJson(managerApp
                                .getSubtaskById(subtask.getId()))
                        .getBytes());
            }
        }

        @SneakyThrows
        private void addNewEpic(String inputBody, HttpExchange exchange) {
            Epic epic = gson.fromJson(inputBody, Epic.class);
            managerApp.addNewEpic(epic);

            exchange.sendResponseHeaders(200, 0);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(gson
                        .toJson(managerApp
                                .getEpicById(epic.getId()))
                        .getBytes());
            }
        }
    }

    static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {

        private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime dateTime) throws IOException {
            jsonWriter.value(dateTime.format(format));
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), format);
        }
    }
}
