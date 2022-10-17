import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.FileBackedTasksManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager("src/main/resources/tasks.csv");
        Task task = new Task("Task", "Description", Status.NEW, 15L, LocalDateTime.now());
        Epic epic = new Epic("Task", "Description", Status.NEW);
        Subtask subtask1 = new Subtask("Sub", "Description", 2, 23L, LocalDateTime.now());
        Subtask subtask2 = new Subtask("Sub", "Description", 2, 17L, LocalDateTime.now());
        Subtask subtask3 = new Subtask("Sub", "Description", 2, 20L, LocalDateTime.now());
        manager.addNewTask(task);
        manager.addNewEpic(epic);
        manager.addNewSubtask(subtask1);
        manager.addNewSubtask(subtask2);
        manager.addNewSubtask(subtask3);
        System.out.println(manager.getSubtasks());
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubtasksByEpic(2));
    }
}
