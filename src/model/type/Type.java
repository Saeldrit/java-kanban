package model.type;

public enum Type {
    TASK("Task"),
    EPIC("Epic"),
    SUBTASK("Subtask");

    private final String title;

    Type(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
