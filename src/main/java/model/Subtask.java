package model;

import model.status.Status;

import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private Integer epicId;

    public Subtask() {

    }

    public Subtask(String title, String description,
                   int epicId,
                   Long duration, LocalDateTime startTime) {
        super(title, description, Status.NEW, duration, startTime);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "Subtask{"
                + "id - " + getId()
                + "; title - " + getTitle()
                + "; desc - " + getDescription()
                + "; epic id - " + getEpicId()
                + "; status - " + getStatus();
    }
}
