package service.manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.Map;

public interface Manager {
    void addNewTask(Task task);

    void updateTask(Task task);

    Map<Integer, Task> getTasks();

    Map<Integer, Subtask> getSubtasks();

    Map<Integer, Epic> getEpics();

    Task getTaskById(int id);

    void removeTasks();

    void removeSubtasks();

    void removeEpics();

    void removeTaskById(int id);

    Map<Integer, Subtask> getSubtasksByEpic(Epic epic);
}
