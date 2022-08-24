package service.manager_interface.task_manager;

import model.Task;

import java.util.List;

public abstract class ManagerApp implements ManagerTask, ManagerSubtask, ManagerEpic {
    public abstract List<Task> getTaskViewHistory();
}
