import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task", "Description", Status.NEW, 15L, LocalDateTime.now());
        Epic epic = new Epic("Epic", "Description", Status.NEW);
        Subtask subtask1 = new Subtask("Subtask1", "Description", 2, 15L, LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask2", "Description", 2, 20L, LocalDateTime.now());
        Subtask subtask3 = new Subtask("Subtask3", "Description", 2, 25L, LocalDateTime.now());
        taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        System.out.println(epic.getDuration());
        System.out.println(epic.getStartTime());
        System.out.println(epic.getEndTime());

        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        System.out.println(epic.getDuration());
        System.out.println(epic.getStartTime());
        System.out.println(epic.getEndTime());
    }
}
