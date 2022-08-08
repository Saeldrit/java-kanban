package model;

import model.status.Status;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Epic extends Task {

    private final Map<Integer, Subtask> subtaskMap;

    public Epic(String title, String description) {
        super(title, description);
        this.subtaskMap = new LinkedHashMap<>();
    }

    public Map<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    public void setSubtaskMap(Subtask subtask) {
        this.subtaskMap.put(subtask.getId(), subtask);
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskMap, epic.subtaskMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskMap);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskMap=" + subtaskMap +
                '}';
    }
}
