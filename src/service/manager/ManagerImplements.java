package service.manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.LinkedHashMap;
import java.util.Map;

public class ManagerImplements implements Manager {

    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private final Map<Integer, Subtask> subtaskMap;

    public ManagerImplements() {
        this.taskMap = new LinkedHashMap<>();
        this.epicMap = new LinkedHashMap<>();
        this.subtaskMap = new LinkedHashMap<>();
    }

    @Override
    public void addNewTask(Task task) {
        if (task instanceof Epic) {
            epicMap.put(task.getId(), (Epic) task);
        }

        if (task instanceof Subtask) {
            subtaskMap.put(task.getId(), (Subtask) task);
        }

        if (task != null) {
            taskMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateTask(Task task) {
        addNewTask(task);
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return taskMap;
    }

    @Override
    public Map<Integer, Subtask> getSubtasks() {
        return subtaskMap;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epicMap;
    }

    @Override
    public Task getTaskById(int id) {
        if (taskMap.containsKey(id)) {
            return taskMap.get(id);
        } else if (subtaskMap.containsKey(id)) {
            return subtaskMap.get(id);
        } else if (epicMap.containsKey(id)) {
            return epicMap.get(id);
        }
        return null;
    }

    @Override
    public void removeTasks() {
        taskMap.clear();
    }

    @Override
    public void removeSubtasks() {
        subtaskMap.clear();
    }

    @Override
    public void removeEpics() {
        epicMap.clear();
    }

    @Override
    public void removeTaskById(int id) {
        if (taskMap.containsKey(id)) {
            taskMap.remove(id);
        } else if (subtaskMap.containsKey(id)) {
            subtaskMap.remove(id);
        } else epicMap.remove(id);
    }

    @Override
    public Map<Integer, Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtaskMap();
    }
}
