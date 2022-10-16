import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.FileBackedTasksManager;
import service.InMemoryTaskManager;

import java.time.LocalDateTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        FileBackedTasksManager fileManager = new FileBackedTasksManager("src/main/resources/tasks.csv");

        Task task = new Task("Task", "Description", Status.NEW, 15L, LocalDateTime.now());
        Epic epic = new Epic("Epic", "Description", Status.NEW);

        Subtask subtask1 = new Subtask("Subtask1", "Description", 2, 15L, LocalDateTime.now().plusHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Description", 2, 20L, LocalDateTime.now().plusMinutes(12));
        Subtask subtask3 = new Subtask("Subtask3", "Description", 2, 25L, LocalDateTime.now().plusHours(2));


        /*taskManager.addNewTask(task);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
        taskManager.addNewSubtask(subtask3);

        List<Subtask> subtasks = taskManager.getPrioritizedSubtasks();
        subtasks.forEach(sub -> System.out.println(sub.getStartTime()));*/

        fileManager.addNewTask(task);
        fileManager.addNewEpic(epic);
        fileManager.addNewSubtask(subtask1);
        fileManager.addNewSubtask(subtask2);
        fileManager.addNewSubtask(subtask3);

        fileManager.getPrioritizedSubtasks().forEach(System.out::println);
        System.out.println("\n");

        fileManager.getSubtasks().forEach(System.out::println);
    }
}
