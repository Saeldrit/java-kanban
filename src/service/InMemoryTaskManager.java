package service;

import factory.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.manager_interface.HistoryManager;
import service.manager_interface.task_manager.ManagerApp;

import java.util.*;

public class InMemoryTaskManager extends ManagerApp {

    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private final Map<Integer, Subtask> subtaskMap;
    private final HistoryManager historyManager;

    private int identifier;

    public InMemoryTaskManager() {
        this.taskMap = new LinkedHashMap<>();
        this.epicMap = new LinkedHashMap<>();
        this.subtaskMap = new LinkedHashMap<>();
        this.historyManager = Managers.getHistoryManager();
    }

    @Override
    public List<Task> getTaskViewHistory() {
        return historyManager.getHistory();
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
            throw new IllegalArgumentException("Add Epic for subtask");
        }

        subtask.setId(subtaskId);
        subtaskMap.put(subtaskId, subtask);

        epic.addSubtask(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            if (taskMap.containsKey(task.getId())) {
                taskMap.put(task.getId(), task);
            }
        } else {
            throw new IllegalArgumentException("Your Task is Null");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null) {
            updateEpicStatus(epic);
            if (epicMap.containsKey(epic.getId())) {
                epicMap.put(epic.getId(), epic);
            }
        } else {
            throw new IllegalArgumentException("Your Epic is Null");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null) {
            Epic epic = epicMap.get(subtask.getEpicId());

            updateEpicStatus(epic);
            subtaskMap.put(subtask.getId(), subtask);
        } else {
            throw new IllegalArgumentException("Your Subtask is Null");
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
        historyManager.add(taskMap.get(id));
        return taskMap.get(id);
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtaskMap.get(id));
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
        for (var key : epicMap.keySet()) {
            epicMap.get(key).setSubtaskList(new ArrayList<>());
        }
    }

    @Override
    public void removeTaskById(int id) {
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            List<Subtask> subtasks = epic.getSubtask();
            subtasks.forEach(sub -> subtaskMap.remove(sub.getId()));
            epicMap.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            Epic epic = epicMap.get(subtask.getEpicId());
            List<Subtask> subtasks = epic.getSubtask();

            subtasks.remove(subtask);

            if (subtasks.size() == 0) {
                subtaskMap.remove(id);
                epicMap.get(epic.getId()).setSubtaskList(new ArrayList<>());
            } else {
                epic.setSubtaskList(subtasks);
                updateEpic(epic);
                subtaskMap.remove(id);
            }
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
            switch (subtask.getStatus()) {
                case IN_PROGRESS:
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                case DONE:
                    ++counter;
                    break;
                case NEW:
                    isCheck = subtask.getStatus().equals(Status.NEW);
                    break;
            }
        }

        if (counter == subtasks.size()) {
            epic.setStatus(Status.DONE);
        } else if (isCheck && counter < subtasks.size() && counter > 0) {
            epic.setStatus(Status.IN_PROGRESS);
        } else {
            epic.setStatus(Status.NEW);
        }
    }
}
