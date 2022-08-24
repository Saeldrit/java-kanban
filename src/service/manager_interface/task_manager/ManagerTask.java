package service.manager_interface.task_manager;

import model.Task;

import java.util.List;

public interface ManagerTask {
    void addNewTask(Task task);

    void updateTask(Task task);

    List<Task> getTasks();

    Task getTaskById(int id);

    void removeTasks();

    void removeTaskById(int id);
}
