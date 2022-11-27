package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.manager_interface.task_manager.ManagerApp;

import java.time.DateTimeException;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager extends ManagerApp {

    protected final Map<Integer, Task> taskMap;
    protected final Map<Integer, Epic> epicMap;
    protected final Map<Integer, Subtask> subtaskMap;
    private final Set<Task> prioritizedTasks;

    private int identifier;

    public InMemoryTaskManager() {
        this.taskMap = new LinkedHashMap<>();
        this.epicMap = new LinkedHashMap<>();
        this.subtaskMap = new LinkedHashMap<>();
        this.prioritizedTasks = new TreeSet<>(Comparator
                .comparing(Task::getStartTime,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()))
                .thenComparing(Task::getId));
    }

    protected void setIdentifier(int max) {
        this.identifier = max;
    }

    protected int getIdentifier() {
        return identifier;
    }

    @Override
    public int addNewTask(Task task) {
        if (validationIdenticalTasks(task)) {
            if (!taskMap.containsKey(task.getId())) {
                task.setId(++identifier);
                taskMap.put(task.getId(), task);
                prioritizedTasks.add(task);
            }
        } else {
            throw new DateTimeException("Cannot create a task with the same time");
        }
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        if (!epicMap.containsKey(epic.getId())) {
            epic.setId(++identifier);
            epicMap.put(epic.getId(), epic);
            prioritizedTasks.add(epic);
        }
        return epic.getId();
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        if (validationIdenticalTasks(subtask)) {
            int epicId = subtask.getEpicId();

            if (!subtaskMap.containsKey(subtask.getId())) {
                int subtaskId = ++identifier;
                Epic epic = epicMap.get(epicId);

                if (epic == null) {
                    throw new NullPointerException("Add Epic for subtask");
                }

                subtask.setId(subtaskId);
                subtaskMap.put(subtaskId, subtask);

                epic.addSubtask(subtask);
                updateAllEpicProperties(epic);
                prioritizedTasks.add(subtask);
            }
        } else {
            throw new DateTimeException("Cannot create a subtask with the same time");
        }
        return subtask.getId();
    }

    @Override
    public int updateTask(Task task) {
        if (task != null) {
            if (taskMap.containsKey(task.getId())) {
                taskMap.put(task.getId(), task);
                prioritizedTasks.add(task);
                return task.getId();
            }
        } else {
            throw new NullPointerException("Your task is null");
        }
        return -1;
    }

    @Override
    public int updateEpic(Epic epic) {
        if (epic != null) {
            updateAllEpicProperties(epic);
            if (epicMap.containsKey(epic.getId())) {
                epicMap.put(epic.getId(), epic);
                prioritizedTasks.add(epic);
                return epic.getId();
            }
        } else {
            throw new NullPointerException("Your epic is null");
        }
        return -1;
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        int id;

        if (subtask != null) {
            Epic epic = epicMap.get(subtask.getEpicId());
            updateAllEpicProperties(epic);
            subtaskMap.put(subtask.getId(), subtask);

            prioritizedTasks.add(subtask);
            id = subtask.getId();
        } else {
            throw new NullPointerException("Your subtask is null");
        }
        return id;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epicMap.values());
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
        taskMap.values().forEach(e -> historyManager.remove(e.getId()));

        taskMap.clear();
    }

    @Override
    public void removeEpics() {
        epicMap.values().forEach(e -> historyManager.remove(e.getId()));
        subtaskMap.values().forEach(e -> historyManager.remove(e.getId()));
        epicMap.clear();
        subtaskMap.clear();
    }

    @Override
    public void removeSubtasks() {
        subtaskMap.values().forEach(e -> historyManager.remove(e.getId()));
        subtaskMap.clear();

        for (var key : epicMap.keySet()) {
            epicMap.get(key).setSubtaskList(new ArrayList<>());
        }
    }

    @Override
    public void removeTaskById(int id) {
        historyManager.remove(id);
        taskMap.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        if (epicMap.containsKey(id)) {
            Epic epic = epicMap.get(id);
            List<Subtask> subtasks = epic.getSubtaskList();
            subtasks.forEach(sub -> subtaskMap.remove(sub.getId()));
            subtasks.forEach(sub -> historyManager.remove(sub.getId()));
            epicMap.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtaskMap.containsKey(id)) {
            Subtask subtask = subtaskMap.get(id);
            Epic epic = epicMap.get(subtask.getEpicId());
            List<Subtask> subtasks = epic.getSubtaskList();

            subtasks.remove(subtask);

            if (subtasks.size() == 0) {
                subtaskMap.remove(id);
                historyManager.remove(id);
                historyManager.remove(epic.getId());
                epicMap.get(epic.getId()).setSubtaskList(new ArrayList<>());
            } else {
                epic.setSubtaskList(subtasks);
                updateAllEpicProperties(epic);
                subtaskMap.remove(id);
                historyManager.remove(id);
            }
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpic(int epicId) {
        return subtaskMap
                .values()
                .stream()
                .filter(sub -> sub.getEpicId().equals(epicId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void updateAllEpicProperties(Epic epic) {
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtasks = epic.getSubtaskList();
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

    private void updateEpicDuration(Epic epic) {
        epic.setDuration(epic
                .getSubtaskList()
                .stream()
                .mapToLong(Subtask::getDuration)
                .sum());
    }

    private void updateEpicEndTime(Epic epic) {
        epic.setEndTime(epic.getStartTime().plusMinutes(epic.getDuration()));
    }

    private void updateEpicStartTime(Epic epic) {
        epic.setStartTime(epic.getSubtaskList()
                .stream()
                .min(Comparator.comparing(
                        Subtask::getStartTime))
                .orElseThrow()
                .getStartTime());
    }

    private boolean validationIdenticalTasks(Task newTask) {
        return prioritizedTasks
                .stream()
                .noneMatch(task -> task.getStartTime()
                        .isEqual(newTask.getStartTime()));
    }
}
