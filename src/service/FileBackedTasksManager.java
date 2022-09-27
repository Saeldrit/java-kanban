package service;

import factory.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import model.type.TypeTask;
import repository.composer.AbstractHandlerOfInformation;

import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String FIRST_LINE = "id,type,name,status,description,epic";
    private final String filePath;
    private final List<String> contentForWriteInTheFile;
    private final AbstractHandlerOfInformation handler;

    public FileBackedTasksManager(String filePath) {
        this.filePath = filePath;
        this.contentForWriteInTheFile = new ArrayList<>();
        this.handler = Managers.getHandler();
        createTasks(handler.readContentFromFile(filePath));
    }

    private void save() {
        fillInContentToFile();
        handler.writeContentToFile(filePath, contentForWriteInTheFile);
    }

    private void fillInContentToFile() {
        contentForWriteInTheFile.clear();
        contentForWriteInTheFile.add(FIRST_LINE);
        fillInTasks();
        fillInEpics();
        fillInSubtasks();
        contentForWriteInTheFile.add(historyToLine());
    }

    private void fillInTasks() {
        getTasks().forEach(task -> contentForWriteInTheFile.add(task.toString()));
    }

    private void fillInEpics() {
        getEpics().forEach(epic -> contentForWriteInTheFile.add(epic.toString()));
    }

    private void fillInSubtasks() {
        getSubtasks().forEach(sub -> contentForWriteInTheFile.add(sub.toString()));
    }

    private String historyToLine() {
        StringBuilder history = new StringBuilder();

        history.append("\n");
        getTasksViewHistory().forEach(task -> history.append(task.getId()).append(","));
        history.deleteCharAt(history.length() - 1);

        return history.toString();
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTasks() {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() {
        super.removeSubtasks();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    public void createTasks(List<String> content) {
        for (var line : content) {
            if (!line.isEmpty()) {

                String[] split = line.split(",");
                String firstSymbol = split[0];

                if (!firstSymbol.equals("id")) {
                    switch (TypeTask.valueOf(split[1]).ordinal()) {
                        case 0:
                            Task task = packTask(split);
                            super.taskMap.put(task.getId(), task);
                            break;
                        case 1:
                            Epic epic = packEpic(split);
                            super.epicMap.put(epic.getId(), epic);
                            break;
                        case 2:
                            Subtask subtask = packSubtask(split);
                            super.subtaskMap.put(subtask.getId(), subtask);
                            break;
                    }
                }
            } else {
                return;
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
        int number;
        try {
            number = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            number = -1;
        }
        return number;
    }
}
