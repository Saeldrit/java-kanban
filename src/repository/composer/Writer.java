package repository.composer;

import java.util.List;

public interface Writer {
    void writeContentToFile(String path, List<String> content);
}
