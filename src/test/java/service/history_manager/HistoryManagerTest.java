package service.history_manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        initialTask();
    }

    @AfterEach
    public void clear() {
        taskManager.history().clear();
    }

    private void initialTask() {
        Task task = new Task("task", "description", Status.NEW);
        taskManager.addNewTask(task);
        Epic epic = new Epic("epic", "description", Status.NEW);
        taskManager.addNewEpic(epic);
        Subtask subtask1 = new Subtask("subtask", "description", epic.getId());
        Subtask subtask2 = new Subtask("subtask", "description", epic.getId());
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);
    }

    @DisplayName("Сохранить только одну задачу из одинаковых 5 просмотренных")
    @Test
    public void shouldAddToHistoryOnlyOneTaskOutOfFiveIdentical() {
        taskManager.getTaskById(1);
        int initialSize = taskManager.history().size();

        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);

        assertEquals(initialSize, taskManager.history().size());
    }

    @DisplayName("Вернуть пустой список истории до первого вызова")
    @Test
    public void shouldReturnNullListHistoryWithoutGetTaskByID() {
        List<Task> taskList = taskManager.history();
        assertTrue(taskList.isEmpty());

        taskManager.getTaskById(1);
        assertFalse(taskManager.history().isEmpty());
    }

    @DisplayName("Вернуть последовательность 2, 1, 3")
    @Test
    public void shouldReturnHistoryBrowsingOfTask() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);
        taskManager.getEpicById(2);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(3);

        checkTasksBrowsingHistory(List.of(2, 1, 3));
    }

    @Test
    public void shouldRemoveFirstTaskFromForward() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        taskManager.removeTaskById(1);

        checkTasksBrowsingHistory(List.of(2, 3));
    }

    @DisplayName("Удалить все подзадачи если удален эпик")
    @Test
    public void shouldRemoveTaskFromMiddle() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        taskManager.removeEpicById(2);

        checkTasksBrowsingHistory(List.of(1));
    }

    @DisplayName("Удалить последнюю подзадачу и эпик")
    @Test
    public void shouldRemoveLastTaskFromFeed() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);

        taskManager.removeSubtaskById(4);

        checkTasksBrowsingHistory(List.of(1, 2, 3));
    }

    @DisplayName("Удалить эпик если все подзадачи удалены")
    @Test
    public void shouldRemoveAllSubtasksAndEpic() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(2);
        taskManager.getSubtaskById(3);
        taskManager.getSubtaskById(4);

        taskManager.removeSubtaskById(4);
        taskManager.removeSubtaskById(3);

        checkTasksBrowsingHistory(List.of(1));
    }

    private void checkTasksBrowsingHistory(List<Integer> expected) {
        List<Integer> actual = new ArrayList<>();
        taskManager.history().forEach(task -> actual.add(task.getId()));

        assertEquals(expected, actual);
    }
}
