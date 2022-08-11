package model;

import model.status.Status;

import java.util.*;

public class Epic extends Task {

    private List<Subtask> subtaskList;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
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
        return Objects.equals(subtaskList, epic.subtaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIdList=" + subtaskList
                + "title='" + super.getTitle() + '\''
                + ", status=" + super.getStatus()
                + ", id=" + super.getId() + '}';
    }
}
