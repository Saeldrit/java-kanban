package repository;

import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import repository.composer.Builder;
import service.manager_interface.HistoryManager;

import java.util.*;

public class CompilerTask implements Builder {

    private final Map<Integer, Task> taskMap;
    private final Map<Integer, Epic> epicMap;
    private final Map<Integer, Subtask> subtaskMap;

    public CompilerTask() {
        this.taskMap = new HashMap<>();
        this.epicMap = new HashMap<>();
        this.subtaskMap = new HashMap<>();
    }

    public List<Task> getTasks() {
        Collection<Task> tasks = taskMap.values();
        return new ArrayList<>(tasks);
    }

    public List<Subtask> getSubtasks() {
        Collection<Subtask> subtasks = subtaskMap.values();
        return new ArrayList<>(subtasks);
    }

    public List<Epic> getEpics() {
        Collection<Epic> epics = epicMap.values();
        return new ArrayList<>(epics);
    }

    @Override
    public void createTasks(List<String> content) {

        for (int i = 1; i < content.size(); i++) {
            String line = content.get(i);

            if (!(line.isEmpty())) {
                String[] split = line.split(",");
                String type = split[1];

                switch (type.toLowerCase()) {
                    case "task":
                        Task task = packTask(split);
                        taskMap.put(task.getId(), task);
                        break;
                    case "epic":
                        Epic epic = packEpic(split);
                        epicMap.put(epic.getId(), epic);
                        break;
                    case "subtask":
                        Subtask subtask = packSubtask(split);
                        subtaskMap.put(subtask.getId(), subtask);
                        break;
                }
            }
        }
    }

    @Override
    public boolean lookForTask(int id) {
        return taskMap.containsKey(id)
                || epicMap.containsKey(id)
                || subtaskMap.containsKey(id);
    }

    @Override
    public String historyToString(HistoryManager manager) {
        List<Task> history = manager.getHistory();
        StringBuilder historyLine = new StringBuilder();

        history.forEach(task -> historyLine.append(task.getId()).append(","));
        return historyLine.toString();
    }

    private Task packTask(String... split) {
        Task task = new Task();

        for (int i = 0; i < split.length; i++) {
            builder(task, i, split);
        }
        return task;
    }

    private Epic packEpic(String... split) {
        Epic epic = new Epic();

        for (int i = 0; i < split.length; i++) {
            builder(epic, i, split);
        }
        return epic;
    }

    private Subtask packSubtask(String... split) {
        Subtask subtask = new Subtask();

        for (int i = 0; i < split.length; i++) {
            builder(subtask, i, split);
        }
        return subtask;
    }

    private void builder(Task task, int i, String... array) {
        switch (i) {
            case 0:
                task.setId(checkParseInt(array[i]));
                break;
            case 2:
                task.setTitle(array[i]);
                break;
            case 3:
                task.setStatus(Status.valueOf(array[i]));
                break;
            case 4:
                task.setDescription(array[i]);
                break;
            case 5:
                setEpicIdForSubtask((Subtask) task, checkParseInt(array[i]));
                break;
        }
    }

    private void setEpicIdForSubtask(Subtask subtask, int epicId) {
        subtask.setEpicId(epicId);
    }

    private int checkParseInt(String str) {
        int number = -1;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return number;
    }
}
