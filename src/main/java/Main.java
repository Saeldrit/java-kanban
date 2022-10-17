import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import service.FileBackedTasksManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = new FileBackedTasksManager("src/main/resources/tasks.csv");
        Task task1 = Task.builder().id(1)
                .title("Task1")
                .description("description")
                .status(Status.NEW)
                .duration(15L)
                .startTime(LocalDateTime.now().plusMinutes(20))
                .build();

        Set<Task> prioritizedTasks = new TreeSet<>(Comparator
                .comparing(Task::getStartTime,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()))
                .thenComparing(Task::getId));

        prioritizedTasks.add(task1);
        task1.setStatus(Status.DONE);
        task1.setStartTime(LocalDateTime.now());
        prioritizedTasks.add(task1);
        prioritizedTasks.forEach(System.out::println);
    }
}
