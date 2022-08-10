package service.manager_interface;

import model.Epic;
import model.Subtask;

import java.util.List;

public interface ManagerEpic {
    void addNewEpic(Epic epic);

    void updateEpic(Epic epic);

    List<Epic> getEpics();

    Epic getEpicById(int id);

    void removeEpics();

    void removeEpicById(int id);

    List<Subtask> getSubtasksByEpic(Epic epic);
}
