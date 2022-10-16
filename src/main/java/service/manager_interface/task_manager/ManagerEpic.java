package service.manager_interface.task_manager;

import model.Epic;
import model.Subtask;

import java.util.List;

public interface ManagerEpic {
    int addNewEpic(Epic epic);

    int updateEpic(Epic epic);

    List<Epic> getEpics();

    Epic getEpicById(int id);

    void removeEpics();

    void removeEpicById(int id);

    List<Subtask> getSubtasksByEpic(int epicId);

    List<Epic> getPrioritizedEpics();
}
