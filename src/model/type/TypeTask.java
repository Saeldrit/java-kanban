package model.type;

public enum TypeTask {
    TASK("Task"),
    EPIC("Epic"),
    SUBTASK("Subtask");

    private final String title;

    TypeTask(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
