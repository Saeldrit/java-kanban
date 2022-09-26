package service;

import model.Epic;
import model.Subtask;
import model.Task;
import repository.CompilerTask;
import repository.HandlerOfInformationInFile;
import repository.Road;

import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String FIRST_LINE;
    private final HandlerOfInformationInFile handler;
    private final CompilerTask compilerTask;
    private final List<String> content;

    static {
        FIRST_LINE = "id,type,name,status,description,epic";
    }

    public FileBackedTasksManager() {
        this.handler = new HandlerOfInformationInFile();
        this.compilerTask = new CompilerTask();
        this.content = handler.readContentFromFile(Road.ROAD.getTitle());
        compilerTask.createTasks(content);
    }

    public void save(Task task) {
        if (checkFileSize()) {
            handler.writeContentToFile(Road.ROAD.getTitle(), List.of(FIRST_LINE));
        } else {
            handler.writeContentToFile(Road.ROAD.getTitle(), List.of(task.toString()));
        }
    }

    private boolean checkFileSize() {
        return content.isEmpty();
    }

    private boolean checkIfTaskInTheFile(int id) {
        return !compilerTask.lookForTask(id);
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        if (checkIfTaskInTheFile(task.getId())) {
            save(task);
        }
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        if (checkIfTaskInTheFile(epic.getId())) {
            save(epic);
        }
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        if (checkIfTaskInTheFile(subtask.getId())) {
            save(subtask);
        }
    }
}
