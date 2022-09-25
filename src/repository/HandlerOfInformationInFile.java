package repository;

import repository.composer.Reader;
import repository.composer.Writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class HandlerOfInformationInFile implements Reader, Writer {

    @Override
    public List<String> readContentFromFile(String path) {
        List<String> contentOfFile = new ArrayList<>();

        try {
            if (Files.exists(Paths.get(path))) {
                contentOfFile = Files.readAllLines(Paths.get(path));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentOfFile;
    }

    @Override
    public void writeContentToFile(String path, List<String> content) {
        try {
            Files.write(Paths.get(path), content,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
