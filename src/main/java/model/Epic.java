package model;

import lombok.*;
import model.status.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "subtaskList")
@AllArgsConstructor
public class Epic extends Task {

    private List<Subtask> subtaskList;
    private LocalDateTime endTime;

    @Builder(builderMethodName = "epicBuilder")
    public Epic(String title, String description,
                Status status) {
        super(title, description, status, 0L, LocalDateTime.now());
        subtaskList = new ArrayList<>();
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
