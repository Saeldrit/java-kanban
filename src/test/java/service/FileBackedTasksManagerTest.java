package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.manager_interface.task_manager.ManagerAppTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTasksManagerTest extends ManagerAppTest<FileBackedTasksManager> {

    private File file;

    @BeforeEach
    public void beforeEach() {
        file = new File("src/main/resources/tasks.csv");
        taskManager = new FileBackedTasksManager(file.getPath());
        initTask();
        initSubtaskAndEpic();
    }

    @AfterEach
    public void removeEach() {
        boolean isDelete = file.delete();
        assertTrue(isDelete);
    }
}
