package service.manager_interface.task_manager;

import model.Task;

import java.util.List;

public interface ManagerTask {
    int addNewTask(Task task);

    int updateTask(Task task);

    List<Task> getTasks();

    Task getTaskById(int id);

    void removeTasks();

    void removeTaskById(int id);

    List<Task> getPrioritizedTasks();
}
