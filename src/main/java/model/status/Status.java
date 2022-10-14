package model.status;

public enum Status {
    NEW("New Task"),
    IN_PROGRESS("In Progress"),
    DONE("Completed Tasks");

    private final String title;

    Status(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
