package service;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.manager_interface.task_manager.ManagerAppTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTaskManagerTest extends ManagerAppTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
        initTask();
        initSubtaskAndEpic();
    }

    @DisplayName("Инкремент счетчика id при добавлении задачи")
    @Test
    public void shouldIncrementIdentifierAfterAddTask() {
        int initialValue = taskManager.getIdentifier();
        taskManager.addNewTask(Task.builder()
                .duration(15L)
                .startTime(LocalDateTime.now().minusDays(1))
                .build());

        assertTrue(initialValue < taskManager.getIdentifier());
    }
}
