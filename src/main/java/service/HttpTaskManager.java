package service;

import com.google.gson.Gson;
import factory.Managers;
import http.client.KVTaskClient;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;
    private final Gson gson;

    public HttpTaskManager(String urlToServer) {
        this.kvTaskClient = new KVTaskClient(urlToServer);
        gson = Managers.getGson();
    }

    @Override
    protected void save() {
        putTaskToServer();
        putEpicToServer();
        putSubtaskToServer();
    }

    private void putTaskToServer() {
        super.getTasks().forEach(
                task -> kvTaskClient.put(
                        String.valueOf(task.getId()),
                        gson.toJson(task)));
    }

    private void putEpicToServer() {
        super.getEpics().forEach(
                task -> kvTaskClient.put(
                        String.valueOf(task.getId()),
                        gson.toJson(task)));
    }

    private void putSubtaskToServer() {
        super.getSubtasks().forEach(
                task -> kvTaskClient.put(
                        String.valueOf(task.getId()),
                        gson.toJson(task)));
    }

    @Override
    public int addNewTask(Task task) {
        return super.addNewTask(task);
    }

    @Override
    public int addNewEpic(Epic epic) {
        return super.addNewEpic(epic);
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        return super.addNewSubtask(subtask);
    }

    @Override
    public Task getTaskById(int id) {
        String json = kvTaskClient.load(String.valueOf(id));
        Task task = gson.fromJson(json, Task.class);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        String json = kvTaskClient.load(String.valueOf(id));
        Epic epic = gson.fromJson(json, Epic.class);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        String json = kvTaskClient.load(String.valueOf(id));
        Subtask subtask = gson.fromJson(json, Subtask.class);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        super.getTasks().forEach(task -> {
            String json = kvTaskClient.load(String.valueOf(task.getId()));
            Task taskJson = gson.fromJson(json, Task.class);
            list.add(taskJson);
        });
        return list;
    }

    @Override
    public List<Epic> getEpics() {
        List<Epic> list = new ArrayList<>();
        super.getEpics().forEach(epic -> {
            String json = kvTaskClient.load(String.valueOf(epic.getId()));
            Epic taskJson = gson.fromJson(json, Epic.class);
            list.add(taskJson);
        });
        return list;
    }

    @Override
    public List<Subtask> getSubtasks() {
        List<Subtask> list = new ArrayList<>();
        super.getSubtasks().forEach(task -> {
            String json = kvTaskClient.load(String.valueOf(task.getId()));
            Subtask taskJson = gson.fromJson(json, Subtask.class);
            list.add(taskJson);
        });
        return list;
    }

    @Override
    public int updateTask(Task task) {
        return super.updateTask(task);
    }

    @Override
    public int updateEpic(Epic epic) {
        return super.updateTask(epic);
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        return super.updateTask(subtask);
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
    }
}
