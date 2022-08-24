package service.manager_interface.task_manager;

import model.Subtask;

import java.util.List;

public interface ManagerSubtask {
    void addNewSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    List<Subtask> getSubtasks();

    Subtask getSubtaskById(int id);

    void removeSubtasks();

    void removeSubtaskById(int id);
}
