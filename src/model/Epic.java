package model;

import model.status.Status;

import java.util.*;

public class Epic extends Task {

    private List<Subtask> subtaskIdList;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        subtaskIdList = new ArrayList<>();
    }

    public List<Subtask> getSubtask() {
        return subtaskIdList;
    }

    public void addSubtask(Subtask subtask) {
        subtaskIdList.add(subtask);
    }

    public void setSubtaskIdList(List<Subtask> subtaskIdList) {
        this.subtaskIdList = subtaskIdList;
    }

    @Override
    public Status getStatus() {
        return super.getStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIdList, epic.subtaskIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIdList);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIdList=" + subtaskIdList
                + "title='" + super.getTitle() + '\''
                + ", status=" + super.getStatus()
                + ", id=" + super.getId() + '}';
    }
}
