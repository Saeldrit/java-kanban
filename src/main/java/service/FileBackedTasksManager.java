package service;

import factory.Managers;
import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import model.type.TypeTask;
import repository.composer.ReaderAndWriterHandler;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String FIRST_LINE =
            "id,type,name,status,description,duration,startTime,epic";
    private final File file;
    private final ReaderAndWriterHandler handler;

    public FileBackedTasksManager(String filePath) {
        this.file = new File(filePath);
        this.handler = Managers.getHandler();
        createTasks(handler.readContentFromFile(filePath));
    }

    private void save() {
        List<String> contentForWriteInTheFile = new ArrayList<>();
        fillInContentToFile(contentForWriteInTheFile);
        handler.writeContentToFile(file.getPath(), contentForWriteInTheFile);
    }

    private void fillInContentToFile(List<String> contentForWriteInTheFile) {
        contentForWriteInTheFile.clear();
        contentForWriteInTheFile.add(FIRST_LINE);
        fillInTasks(contentForWriteInTheFile);
        fillInEpics(contentForWriteInTheFile);
        fillInSubtasks(contentForWriteInTheFile);
        contentForWriteInTheFile.add(historyToLine());
    }

    private void fillInTasks(List<String> contentForWriteInTheFile) {
        getTasks().forEach(task -> contentForWriteInTheFile.add(taskToString(task)));
    }

    private void fillInEpics(List<String> contentForWriteInTheFile) {
        getEpics().forEach(epic -> contentForWriteInTheFile.add(taskToString(epic)));
    }

    private void fillInSubtasks(List<String> contentForWriteInTheFile) {
        getSubtasks().forEach(sub -> contentForWriteInTheFile.add(taskToString(sub)));
    }

    private String historyToLine() {
        return "\n"
                + historyManager.getHistory()
                .stream()
                .map(task -> String.valueOf(task.getId()))
                .collect(Collectors.joining(","));
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        int id = super.addNewSubtask(subtask);
        save();
        return id;
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
    public int updateTask(Task task) {
        int id = super.updateTask(task);
        save();
        return id;
    }

    @Override
    public int updateEpic(Epic epic) {
        int id = super.updateEpic(epic);
        save();
        return id;
    }

    @Override
    public int updateSubtask(Subtask subtask) {
        int id = super.updateSubtask(subtask);
        save();
        return id;
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

    public String taskToString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s,%s",
                task.getId(),
                TypeTask.valueOf(task.getClass().getSimpleName().toUpperCase()),
                task.getTitle(),
                task.getStatus(),
                task.getDescription(),
                task.getDuration(),
                task.getStartTime());
    }

    private void createTasks(List<String> content) {
        for (var line : content) {
            if (!line.isEmpty()) {

                String[] split = line.split(",");
                String firstSymbol = split[0];

                if (!firstSymbol.equals("id")) {
                    int identifier = super.getIdentifier();

                    switch (TypeTask.valueOf(split[1]).ordinal()) {
                        case 0:
                            Task task = packTask(split);
                            super.taskMap.put(task.getId(), task);
                            setIdentifierTask(identifier, task);
                            break;
                        case 1:
                            Epic epic = packEpic(split);
                            super.epicMap.put(epic.getId(), epic);
                            setIdentifierTask(identifier, epic);
                            break;
                        case 2:
                            Subtask subtask = packSubtask(split);
                            super.subtaskMap.put(subtask.getId(), subtask);
                            setIdentifierTask(identifier, subtask);
                            break;
                    }
                }
            } else {
                return;
            }
        }
    }

    private void setIdentifierTask(int identifier, Task task) {
        super.setIdentifier(identifier < task.getId() ? task.getId() : identifier);
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
                task.setDuration(Long.parseLong(array[i]));
                break;
            case 6:
                task.setStartTime(LocalDateTime.parse(array[i]));
                break;
            case 7:
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
