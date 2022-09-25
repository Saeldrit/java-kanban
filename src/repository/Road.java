package repository;

public enum Road {
    ROAD("resources/tasks.csv");

    private final String title;

    Road(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
