package test;

import model.Epic;
import model.Subtask;
import model.status.Status;
import service.ManagerImplements;

public class Test {
    ManagerImplements manager = new ManagerImplements();

    public void startTest() {
        Epic epic = new Epic("Переезд", "Уехать в Питер до конца недели", Status.NEW);
        manager.addNewEpic(epic);

        Subtask subtask = new Subtask("Ноутбук", "Не забыть зарядное устройство", epic.getId());
        Subtask subtask1 = new Subtask("Купить зонтик", "Мы в Питер едем, как бы...", epic.getId());
        manager.addNewSubtask(subtask);
        manager.addNewSubtask(subtask1);

        Epic epic1 = new Epic("Съездить в сады", "Нужно набрать яблок", Status.NEW);
        manager.addNewEpic(epic1);

        Subtask subtask2 = new Subtask("Заправиться", "Иначе можем не доехать", epic1.getId());
        manager.addNewSubtask(subtask2);

        manager.getEpics().forEach(System.out::println);

        testForFirstEpic(epic, subtask, subtask1);

        printCheckRemoveSubtask(epic1, subtask2);
    }

    private void testForFirstEpic(Epic epic, Subtask...subtask) {
        subtask[0].setStatus(Status.DONE);
        subtask[1].setStatus(Status.DONE);
        manager.updateSubtask(subtask[0]);
        manager.updateSubtask(subtask[1]);
        printCheckStatus(epic, Status.DONE);

        subtask[1].setStatus(Status.NEW);
        manager.updateSubtask(subtask[1]);
        printCheckStatus(epic, Status.IN_PROGRESS);
    }

    private void printCheckStatus(Epic epic, Status status) {
        if (manager.getEpicById(epic.getId()).getStatus().equals(status)) {
            System.out.format(("Status of the Epic by id %s SUCCESSFULLY\n"), epic.getId());
        } else {
            System.out.println("FAILED");
        }
    }

    private void printCheckRemoveSubtask(Epic epic, Subtask subtask) {
        manager.removeSubtaskById(subtask.getId());

        if (!manager.getEpics().contains(epic)) {
            System.out.println("Remove done SUCCESSFULLY");
        } else {
            System.out.println("FAILED");
        }
    }
}
