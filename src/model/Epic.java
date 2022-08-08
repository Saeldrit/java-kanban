package model;

import model.status.Status;

import java.util.LinkedHashMap;
import java.util.Map;

public class Epic extends Task {

    private final Map<Integer, Subtask> subtaskMap;

    public Epic(String title, String description) {
        super(title, description);
        this.subtaskMap = new LinkedHashMap<>();
    }

    public Map<Integer, Subtask> getSubtaskMap() {
        return subtaskMap;
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }
}
