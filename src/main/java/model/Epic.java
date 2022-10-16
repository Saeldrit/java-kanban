package model;

import model.status.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    private List<Subtask> subtaskList;
    private LocalDateTime endTime;

    public Epic() {

    }

    public Epic(String title, String description,
                Status status) {
        super(title, description, status, 0L, LocalDateTime.now());
        subtaskList = new ArrayList<>();
    }

    public List<Subtask> getSubtask() {
        return subtaskList;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskList, epic.subtaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }

    @Override
    public String toString() {
        return "Epic{"
                + "id - " + getId()
                + "; title - " + getTitle()
                + "; desc - " + getDescription()
                + "; subtask - " + subtaskList
                + "; status - " + getStatus();
    }
}
