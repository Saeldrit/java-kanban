package repository.composer;

import java.util.List;

public interface ReaderAndWriterHandler {
    List<String> readContentFromFile(String path);

    void writeContentToFile(String path, List<String> content);
}
