package repository.composer;

import java.util.List;

public interface Reader {
    List<String> readContentFromFile(String path);
}
