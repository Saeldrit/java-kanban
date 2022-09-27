package repository;

import repository.composer.AbstractHandlerOfInformation;
import throwable.exceptions.ManagerSaveException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class HandlerOfInformationInFile extends AbstractHandlerOfInformation {

    @Override
    public List<String> readContentFromFile(String path) {
        List<String> contentOfFile = new ArrayList<>();

        try {
            if (Files.exists(Paths.get(path))) {
                contentOfFile = Files.readAllLines(Paths.get(path));
            } else {
                Files.createFile(Paths.get(path));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Not Found Path to File");
        }

        return contentOfFile;
    }

    @Override
    public void writeContentToFile(String path, List<String> content) {
        try (BufferedWriter buffer = new BufferedWriter(new FileWriter(path))) {
            for (var line : content) {
                buffer.write(line);
                buffer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Not Found Path to File");
        }
    }
}
