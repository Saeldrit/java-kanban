package repository;

import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import repository.composer.Builder;

import java.util.ArrayList;
import java.util.List;

public class CompilerTask implements Builder {

    private final List<Task> tasks;
    private final List<Epic> epics;
    private final List<Subtask> subtasks;

    public CompilerTask() {
        this.tasks = new ArrayList<>();
        this.epics = new ArrayList<>();
        this.subtasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Epic> getEpics() {
        return epics;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
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
                        tasks.add(packTask(split));
                        break;
                    case "epic":
                        epics.add(packEpic(split));
                        break;
                    case "subtask":
                        subtasks.add(packSubtask(split));
                        break;
                }
            }
        }
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
