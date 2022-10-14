package repository;

import factory.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.composer.ReaderAndWriterHandler;
import throwable.exceptions.ManagerSaveException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HandlerOfInformationInFileTest {

    private ReaderAndWriterHandler readerAndWriter;
    private File file;

    @BeforeEach
    public void beforeEach() {
        readerAndWriter = Managers.getHandler();
        file = new File("src/main/resources/tasks.csv");
    }

    @DisplayName("Выкинуть MSE если путь к файлу для чтения битый")
    @Test
    public void shouldMSEIfFilePathInvalid() {
        assertThrows(ManagerSaveException.class,
                () -> readerAndWriter.readContentFromFile("resourcess/tasks.csv"));
    }

    @DisplayName("Создать файл если его нет по адресу")
    @Test
    public void shouldMSEIfFilePathInvalids() {
        assertTrue(file.delete());
        boolean isExist = Files.exists(Paths.get(file.getPath()));

        readerAndWriter.readContentFromFile(file.getPath());
        boolean isExistAfterReader = Files.exists(Paths.get(file.getPath()));

        assertNotEquals(isExist, isExistAfterReader);
    }

    @DisplayName("Прочитать содержимое файла")
    @Test
    public void shouldReaderContentFromFileByPath() {
        List<String> content = readerAndWriter.readContentFromFile(file.getPath());
        assertNotNull(content);
    }

    @DisplayName("Выкинуть MSE если путь к файлу битый для записи")
    @Test
    public void shouldThrowMSEIfFilePathInvalid() {
        assertThrows(ManagerSaveException.class,
                () -> readerAndWriter.writeContentToFile("resourcess/tasks.csv", List.of("test")));
    }

    @DisplayName("Записать контент в файл")
    @Test
    public void shouldWriterContentToFile() {
        var content = List.of("Test 1", "Test 2", "Test 3");

        readerAndWriter.writeContentToFile(file.getPath(), content);

        assertTrue(file.exists());
        assertNotNull(readerAndWriter.readContentFromFile(file.getPath()));
    }
}
