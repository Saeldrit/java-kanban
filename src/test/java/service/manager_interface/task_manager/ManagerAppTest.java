package service.manager_interface.task_manager;

import model.Epic;
import model.Subtask;
import model.Task;
import model.status.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class ManagerAppTest<T extends ManagerApp> {

    protected T taskManager;

    protected void initTask() {
        taskManager.addNewTask(Task.builder()
                .id(1)
                .title("Task1")
                .description("description")
                .status(Status.NEW)
                .duration(15L)
                .startTime(LocalDateTime.now().minusHours(1))
                .build());

        taskManager.addNewTask(Task.builder()
                .id(2)
                .title("Task2")
                .description("description")
                .status(Status.NEW)
                .duration(15L)
                .startTime(LocalDateTime.now().plusHours(1))
                .build());
    }

    protected void initSubtaskAndEpic() {
        Epic epic = Epic.epicBuilder()
                .title("Epic1")
                .description("Description")
                .status(Status.NEW)
                .build();

        taskManager.addNewEpic(epic);

        taskManager.addNewSubtask(
                Subtask.subtaskBuilder()
                        .title("Subtask1").description("Description")
                        .epicId(epic.getId())
                        .duration(10L)
                        .startTime(LocalDateTime.now().plusMinutes(16))
                        .build());

        taskManager.addNewSubtask(
                Subtask.subtaskBuilder()
                        .title("Subtask2").description("Description")
                        .epicId(epic.getId())
                        .duration(15L)
                        .startTime(LocalDateTime.now().plusHours(13))
                        .build());

        taskManager.addNewSubtask(
                Subtask.subtaskBuilder()
                        .title("Subtask3").description("Description")
                        .epicId(epic.getId())
                        .duration(25L)
                        .startTime(LocalDateTime.now().plusMinutes(13))
                        .build());
    }

    @DisplayName("Добавить новую задачу")
    @Test
    public void shouldMakeNewTask() {
        Task task = taskManager.getTasks().get(0);
        Task expectedTask = taskManager.getTaskById(task.getId());

        assertNotNull(expectedTask, "Задача не найдена.");
        assertEquals(task, expectedTask, "Задачи не совпадают.");
        taskManager.getTasks().forEach(System.out::println);
    }

    @DisplayName("Добавить новый Эпик")
    @Test
    public void shouldMakeNewEpic() {
        Epic epic = taskManager.getEpics().get(0);

        Epic expectedEpic = taskManager.getEpicById(epic.getId());

        assertNotNull(expectedEpic, "Задача не найдена.");
        assertEquals(epic, expectedEpic, "Задачи не совпадают.");
    }

    @DisplayName("Добавить подзадачу и эпик")
    @Test
    void shouldMakeNewSubtask() {
        Subtask subtask = taskManager.getSubtasks().get(0);

        Subtask expectedSubtask = taskManager.getSubtaskById(subtask.getId());

        assertNotNull(expectedSubtask, "Задача не найдена.");
        assertEquals(subtask, expectedSubtask, "Задачи не совпадают.");
    }

    @DisplayName("Выкинуть NPE при создании подзадачи без Эпика")
    @Test
    public void shouldThrowNPEWhenCreatingSubtaskWithoutEpic() {
        int epicId = taskManager.getEpics().size() * 10;
        Subtask newSubtask = new Subtask("Subtask1", "Description",
                epicId, 15L, LocalDateTime.now());

        assertThrows(NullPointerException.class, () -> taskManager.addNewSubtask(newSubtask));
    }

    @DisplayName("Изменить состояние задачи id/description/title/status")
    @Test
    public void shouldUpdateTask() {
        Task task = taskManager.getTaskById(1);
        assertNotNull(task);

        int id = task.getId();
        String description = task.getDescription();
        String title = task.getTitle();
        Status status = task.getStatus();

        task.setId(taskManager.getTasks().size() * 10);
        task.setDescription("Update");
        task.setTitle("Update Task");
        task.setStatus(Status.IN_PROGRESS);
        task.setDuration(28L);
        task.setStartTime(LocalDateTime.now().plusHours(12));

        int result = taskManager.updateTask(task);

        assertEquals(-1, result);
        assertNotEquals(id, task.getId());
        assertNotEquals(description, task.getDescription());
        assertNotEquals(title, task.getTitle());
        assertNotEquals(status, task.getStatus());
    }

    @DisplayName("Выкинуть NPE при обновлении null задачи")
    @Test
    public void shouldThrowNPEByUpdateTask() {
        assertThrows(NullPointerException.class, () -> taskManager.updateTask(null));
    }

    @DisplayName("Изменить состояние эпика id/description/title")
    @Test
    public void shouldUpdateEpicWithoutStatus() {
        Epic epic = taskManager.getEpics().get(0);
        taskManager.addNewEpic(epic);
        assertNotNull(epic);

        int id = epic.getId();
        String description = epic.getDescription();
        String title = epic.getTitle();
        var subtaskList = epic.getSubtaskList();

        epic.setDescription("Update");
        epic.setId(taskManager.getEpics().size() * 10);
        epic.setTitle("Update Epic");
        epic.setSubtaskList(
                List.of(
                        new Subtask("Subtask1", "Description",
                                epic.getId(), 15L, LocalDateTime.now())));

        int result = taskManager.updateEpic(epic);

        assertEquals(-1, result);
        assertNotEquals(id, epic.getId());
        assertNotEquals(description, epic.getDescription());
        assertNotEquals(title, epic.getTitle());
        assertNotEquals(subtaskList, epic.getSubtaskList());
    }

    @DisplayName("Выкинуть NPE при обновлении null Epic")
    @Test
    public void shouldThrowNPEByUpdateEpic() {
        assertThrows(NullPointerException.class, () -> taskManager.updateEpic(null));
    }

    @DisplayName("Изменить состояние подзадачи id/description/title")
    @Test
    public void shouldUpdateSubtaskWithoutStatus() {
        Epic epic = taskManager.getEpics().get(0);
        Subtask subtask = epic.getSubtaskList().get(0);
        assertNotNull(subtask);

        int subtasksSize = taskManager.getSubtasks().size() * 10;

        int id = subtask.getId();
        String title = subtask.getTitle();
        String description = subtask.getDescription();

        subtask.setId(subtasksSize);
        subtask.setTitle("Update");
        subtask.setDescription("Update");

        taskManager.updateSubtask(subtask);

        assertNotEquals(id, subtask.getId());
        assertNotEquals(title, subtask.getTitle());
        assertNotEquals(description, subtask.getDescription());

        subtask.setEpicId(subtasksSize);
        assertThrows(NullPointerException.class, () -> taskManager.updateSubtask(subtask));
    }

    @DisplayName("Вернуть эпик из подзадачи")
    @Test
    public void shouldReturnEpicFromSubtaskId() {
        Subtask subtask = taskManager.getSubtasks().get(0);
        Epic epic = taskManager.getEpicById(subtask.getEpicId());
        assertNotNull(epic);
    }

    @DisplayName("Выкинуть NPE при обновлении null Epic")
    @Test
    public void shouldThrowNPEByUpdateSubtask() {
        assertThrows(NullPointerException.class, () -> taskManager.updateSubtask(null));
    }

    @DisplayName("Вернуть список из двух задач")
    @Test
    public void shouldReturnTasksList() {
        var tasks = taskManager.getTasks();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @DisplayName("Вернуть у эпика NEW если все подзадачи NEW")
    @Test
    public void shouldCheckEpicForNEWStatus() {
        Epic epic = taskManager.getEpics().get(0);
        List<Subtask> subtaskList = epic.getSubtaskList();

        boolean isNew = subtaskList
                .stream()
                .allMatch(sub -> sub.getStatus().equals(Status.NEW));

        assertTrue(isNew);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @DisplayName("Вернуть у эпика DONE если все подзадачи DONE")
    @Test
    public void shouldCheckEpicForDONEStatus() {
        Epic epic = taskManager.getEpics().get(0);
        List<Subtask> subtaskList = epic.getSubtaskList();

        subtaskList.forEach(subtask -> subtask.setStatus(Status.DONE));
        taskManager.updateEpic(epic);

        boolean isDONE = subtaskList
                .stream()
                .allMatch(sub -> sub.getStatus().equals(Status.DONE));

        assertTrue(isDONE);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @DisplayName("Вернуть у эпика IN_PROGRESS если одна из подзадач DONE")
    @Test
    public void shouldCheckEpicForINPROGRESSStatusWhenOneSubHaveDONEStatus() {
        Epic epic = taskManager.getEpics().get(0);
        List<Subtask> subtaskList = epic.getSubtaskList();

        int randomSubId = (int) (Math.random() * subtaskList.size());

        subtaskList.get(randomSubId).setStatus(Status.DONE);
        taskManager.updateEpic(epic);

        boolean isInProgress = subtaskList
                .stream()
                .anyMatch(sub -> sub.getStatus().equals(Status.DONE));

        boolean isNew = subtaskList
                .stream()
                .anyMatch(sub -> sub.getStatus().equals(Status.NEW));

        assertTrue(isInProgress);
        assertTrue(isNew);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @DisplayName("Вернуть у эпика IN_PROGRESS если все подзадачи IN_PROGRESS")
    @Test
    public void shouldCheckEpicForINPROGRESSStatus() {
        Epic epic = taskManager.getEpics().get(0);
        List<Subtask> subtaskList = epic.getSubtaskList();

        subtaskList.forEach(subtask -> subtask.setStatus(Status.IN_PROGRESS));
        taskManager.updateEpic(epic);

        boolean isInProgress = subtaskList
                .stream()
                .allMatch(sub -> sub.getStatus().equals(Status.IN_PROGRESS));

        assertTrue(isInProgress);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @DisplayName("Очистить список задач")
    @Test
    public void shouldReturnZeroSizeIfTasksIsNull() {
        taskManager.removeTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @DisplayName("Очистить список эпиков и подзадач")
    @Test
    public void shouldReturnZeroSizeIfEpicsIsNull() {
        taskManager.removeEpics();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @DisplayName("Очистить список подзадач")
    @Test
    public void shouldReturnZeroSizeIfSubtasksIsNull() {
        taskManager.removeSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
        assertNotEquals(0, taskManager.getEpics().size());
    }

    @DisplayName("Вернуть список задач")
    @Test
    public void shouldReturnTasks() {
        int tasksCount = taskManager.getTasks().size();
        taskManager.addNewTask(Task.builder()
                .duration(15L)
                .startTime(LocalDateTime.now().minusMinutes(12))
                .build());

        assertNotEquals(tasksCount, taskManager.getTasks().size());
    }

    @DisplayName("Вернуть список эпиков")
    @Test
    public void shouldReturnEpics() {
        int epicsCount = taskManager.getEpics().size();
        taskManager.addNewEpic(new Epic());

        assertNotEquals(epicsCount, taskManager.getEpics().size());
    }

    @DisplayName("Вернуть список подзадач")
    @Test
    public void shouldReturnSubtasks() {
        int subtasksCount = taskManager.getSubtasks().size();
        int epicId = taskManager.getEpics().get(0).getId();
        taskManager.addNewSubtask(
                new Subtask("Test", "Test", epicId, 15L, LocalDateTime.now()));

        assertNotEquals(subtasksCount, taskManager.getSubtasks().size());
    }

    @DisplayName("Вернуть задачу по ID")
    @Test
    public void shouldReturnTaskById() {
        int taskId = taskManager.getTasks().get(0).getId();
        Task task = taskManager.getTaskById(taskId);

        assertNotNull(task);
    }

    @DisplayName("Вернуть эпик по ID")
    @Test
    public void shouldReturnEpicById() {
        int epicId = taskManager.getEpics().get(0).getId();
        Epic epic = taskManager.getEpicById(epicId);

        assertNotNull(epic);
    }

    @DisplayName("Вернуть подзадачу по ID")
    @Test
    public void shouldReturnSubtaskById() {
        int subtaskId = taskManager.getSubtasks().get(0).getId();
        Subtask subtask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(subtask);
    }

    @DisplayName("Вернуть подзадачи по epic id")
    @Test
    public void shouldReturnSubtaskByEpicId() {
        Epic epic = taskManager.getEpics().get(0);
        assertNotNull(epic);

        List<Subtask> subtask = taskManager.getSubtasksByEpic(epic.getId());
        assertNotNull(subtask);
    }

    @DisplayName("Выкинуть NPE если задача по ID null")
    @Test
    public void shouldThrowNPEIfTaskByIdIsNull() {
        int size = taskManager.getTasks().size();

        assertThrows(NullPointerException.class, () -> taskManager.getTaskById(size * 10));
    }

    @DisplayName("Выкинуть NPE если эпик по ID null")
    @Test
    public void shouldThrowNPEIfEpicByIdIsNull() {
        int size = taskManager.getEpics().size();

        assertThrows(NullPointerException.class, () -> taskManager.getEpicById(size * 10));
    }

    @DisplayName("Выкинуть NPE если подзадача по ID null")
    @Test
    public void shouldThrowNPEIfSubtaskByIdIsNull() {
        int size = taskManager.getSubtasks().size();

        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(size * 10));
    }

    @DisplayName("Выкинуть NPE при поиске задачи после удаления")
    @Test
    public void shouldThrowNPEByLookForTaskAfterRemoveTaskById() {
        Task task = taskManager.getTasks().get(0);
        assertNotNull(taskManager.getTaskById(task.getId()));

        taskManager.removeTaskById(task.getId());

        assertThrows(NullPointerException.class, () -> taskManager.getTaskById(task.getId()));
    }

    @DisplayName("Выкинуть NPE при поиске эпика после удаления")
    @Test
    public void shouldThrowNPEByLookForEpicAfterRemoveEpicById() {
        Epic epic = taskManager.getEpics().get(0);
        assertNotNull(taskManager.getEpicById(epic.getId()));

        taskManager.removeEpicById(epic.getId());

        assertThrows(NullPointerException.class, () -> taskManager.getTaskById(epic.getId()));
    }

    @DisplayName("Вернуть пустой список подзадач после удаления эпика")
    @Test
    public void shouldReturnZeroSizeSubtasksAfterRemoveEpic() {
        Epic epic = taskManager.getEpics().get(0);
        assertNotNull(taskManager.getEpicById(epic.getId()));

        taskManager.removeEpicById(epic.getId());
        List<Subtask> subtasks = taskManager.getSubtasksByEpic(epic.getId());

        assertEquals(0, subtasks.size());
    }

    @DisplayName("Вернуть NPE при поиске подзадачи после удаления")
    @Test
    public void shouldThrowNPELookForSubtaskAfterRemoveSubtaskById() {
        Subtask subtask = taskManager.getSubtasks().get(0);
        assertNotNull(taskManager.getSubtaskById(subtask.getId()));

        taskManager.removeSubtaskById(subtask.getId());

        assertThrows(NullPointerException.class, () -> taskManager.getTaskById(subtask.getId()));
    }

    @DisplayName("Вернуть пустой список задач после удаления")
    @Test
    public void shouldReturnZeroSizeTasksAfterRemoveList() {
        int initialSize = taskManager.getTasks().size();

        taskManager.removeTasks();
        assertNotEquals(initialSize, taskManager.getTasks().size());
    }

    @DisplayName("Вернуть пустой список эпиков после удаления")
    @Test
    public void shouldReturnZeroSizeEpicsAfterRemoveList() {
        int initialSize = taskManager.getEpics().size();

        taskManager.removeEpics();
        assertNotEquals(initialSize, taskManager.getEpics().size());
    }

    @DisplayName("Вернуть пустой список подзадач после удаления эпиков")
    @Test
    public void shouldReturnZeroSizeEpicsAfterRemoveSubtasksList() {
        int initialSize = taskManager.getSubtasks().size();

        taskManager.removeEpics();
        assertNotEquals(initialSize, taskManager.getSubtasks().size());
    }

    @DisplayName("Вернуть пустой список подзадач после удаления")
    @Test
    public void shouldReturnZeroSizeSubtasksAfterRemoveList() {
        int initialSize = taskManager.getSubtasks().size();

        taskManager.removeSubtasks();
        assertNotEquals(initialSize, taskManager.getSubtasks().size());
    }

    @DisplayName("Вернуть список отсортированных задач по времени")
    @Test
    public void shouldReturnSortedListTasksByTime() {
        Task task1 = taskManager.getPrioritizedTasks().get(0);
        Task task2 = taskManager.getPrioritizedTasks().get(1);

        assertTrue(task1.getStartTime().isBefore(task2.getStartTime()));
    }

    @DisplayName("Вернуть список отсортированных подзадач по времени")
    @Test
    public void shouldReturnSortedSubtasksByTime() {
        Task task1 = taskManager.getPrioritizedTasks().get(0);
        Task task2 = taskManager.getPrioritizedTasks().get(1);

        assertTrue(task1.getStartTime().isBefore(task2.getStartTime()));
    }

    @DisplayName("Посчитать продолжительность выполнения эпика от суммы его подзадач")
    @Test
    public void shouldReturnEpicDurationBySubtasks() {
        Epic epic = taskManager.getEpics().get(0);
        long result = taskManager.getSubtasksByEpic(epic.getId())
                .stream()
                .mapToLong(Subtask::getDuration)
                .sum();

        assertEquals(result, epic.getDuration());
    }

    @DisplayName("Вернуть стартовое время эпика")
    @Test
    public void shouldReturnEpicStartTimeBuSubtasks() {
        Epic epic = taskManager.getEpics().get(0);
        LocalDateTime startTime = taskManager.getSubtasksByEpic(epic.getId())
                .stream().min(Comparator.comparing(
                        Subtask::getStartTime))
                .orElseThrow()
                .getStartTime();

        assertEquals(epic.getStartTime(), startTime);
    }

    @DisplayName("Вернуть конечное время выполнения эпика")
    @Test
    public void shouldReturnEpicEndTimeBuSubtasks() {
        Epic epic = taskManager.getEpics().get(0);

        assertEquals(epic.getEndTime(), epic.getStartTime().plusMinutes(epic.getDuration()));
    }

    @DisplayName("Поругаться, если уже есть задача с одинаковым временем старта")
    @Test
    public void shouldThrowDTEIfTimeEqual() {
        Task task1 = Task.builder().duration(20L).startTime(LocalDateTime.now()).build();
        taskManager.addNewTask(task1);

        assertThrows(DateTimeException.class, () -> taskManager.addNewTask(task1));
    }
}
