package service.manager_interface.task_manager;

import model.Subtask;

import java.util.List;

public interface ManagerSubtask {
    int addNewSubtask(Subtask subtask);

    int updateSubtask(Subtask subtask);

    List<Subtask> getSubtasks();

    Subtask getSubtaskById(int id);

    void removeSubtasks();

    void removeSubtaskById(int id);
}
