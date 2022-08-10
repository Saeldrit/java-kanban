package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.manager_interface.ManagerEpic;
import service.manager_interface.ManagerSubtask;
import service.manager_interface.ManagerTask;

import java.util.*;

public class ManagerImplements implements ManagerTask, ManagerEpic, ManagerSubtask {

    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private final Map<Integer, Subtask> subtaskMap;

    private int identifier;

    public ManagerImplements() {
        this.taskMap = new LinkedHashMap<>();
        this.epicMap = new LinkedHashMap<>();
        this.subtaskMap = new LinkedHashMap<>();
    }

    @Override
    public void addNewTask(Task task) {
        task.setId(++identifier);
        taskMap.put(task.getId(), task);
    }

    @Override
    public void addNewEpic(Epic epic) {
        epic.setId(++identifier);
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        int subtaskId = ++identifier;
        Epic epic = epicMap.get(epicId);

        if (epic == null) {
            throw new NullPointerException("Add Epic for subtask");
        }

        subtask.setId(subtaskId);
        subtaskMap.put(subtaskId, subtask);

        epic.addSubtask(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            updateTaskStatus(task);
            taskMap.put(task.getId(), task);
        } else {
            throw new NullPointerException("Your Task is Null");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            updateEpicStatus(epic);
            epicMap.put(epic.getId(), epic);
        } else {
            throw new NullPointerException("Your Epic is Null");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            Epic epic = epicMap.get(subtask.getEpicId());
            updateSubtaskStatus(subtask);

            updateEpic(epic);
            subtaskMap.put(subtask.getId(), subtask);
        } else {
            throw new NullPointerException("Your Subtask is Null");
        }
    }

    @Override
    public List<Task> getTasks() {
        Collection<Task> tasks = taskMap.values();
        return new ArrayList<>(tasks);
    }

    @Override
    public List<Subtask> getSubtasks() {
        Collection<Subtask> subtasks = subtaskMap.values();
        return new ArrayList<>(subtasks);
    }

    @Override
    public List<Epic> getEpics() {
        Collection<Epic> epics = epicMap.values();
        return new ArrayList<>(epics);
    }

    @Override
    public Task getTaskById(int id) {
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        return subtaskMap.get(id);
    }

    @Override
    public void removeTasks() {
        taskMap.clear();
    }

    @Override
    public void removeEpics() {
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void removeSubtasks() {
        subtaskMap.clear();
        epicMap.clear();
    }

    @Override
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epicMap.get(id);
        List<Subtask> subtasks = epic.getSubtask();

        subtasks.forEach(sub -> subtaskMap.remove(sub.getId()));

        epicMap.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtaskMap.get(id);
        Epic epic = epicMap.get(subtask.getEpicId());
        List<Subtask> subtasks = epic.getSubtask();

        subtasks.remove(subtask);

        if (subtasks.size() == 0) {
            subtaskMap.remove(id);
            epicMap.remove(epic.getId());
        } else {
            epic.setSubtaskIdList(subtasks);
            updateEpic(epic);
            subtaskMap.remove(id);
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return epic.getSubtask();
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtask();
        boolean isCheck = false;
        int counter = 0;

        for (var subtask : subtasks) {
            if (subtask.getStatus().equals(Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (subtask.getStatus().equals(Status.DONE)) {
                ++counter;
            } else {
                isCheck = subtask.getStatus().equals(Status.NEW);
            }
        }

        epic.setStatus(!isCheck && counter == subtasks.size() ? Status.DONE : Status.NEW);
    }

    private void updateTaskStatus(Task task) {
        if (task.getStatus() == Status.NEW
                || task.getStatus() == Status.IN_PROGRESS) {
            task.setStatus(Status.IN_PROGRESS);
        }
    }

    private void updateSubtaskStatus(Subtask subtask) {
        if (subtask.getStatus() == Status.NEW
                || subtask.getStatus() == Status.IN_PROGRESS) {
            subtask.setStatus(Status.IN_PROGRESS);
        }
    }
}
