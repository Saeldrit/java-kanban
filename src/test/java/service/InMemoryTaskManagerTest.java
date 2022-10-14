package service;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.manager_interface.task_manager.ManagerAppTest;

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
        taskManager.addNewTask(new Task());

        assertTrue(initialValue < taskManager.getIdentifier());
    }
}
