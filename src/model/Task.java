package model;

import model.status.Status;
import model.type.TypeTask;

import java.util.Objects;

public class Task {
    private Integer id;
    private String title;
    private String description;
    private Status status;

    public Task() {

    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, status, id);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s",
                getId(),
                TypeTask.TASK,
                getTitle(),
                getStatus(),
                getDescription());
    }
}
