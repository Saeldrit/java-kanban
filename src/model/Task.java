package model;

import model.identifier.Identifier;
import model.status.Status;

import java.util.Objects;

public class Task {
    private final Integer id;
    private String title;
    private String description;
    private Status status;

    public Task(String title, String description) {
        this.id = Identifier.getNextId();
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description)
                && status == task.status && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, status, id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\''
                + ", description='" + description + '\''
                + ", status=" + status
                + ", id=" + id + '}';
    }
}