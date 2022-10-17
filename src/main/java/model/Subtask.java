package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import model.status.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Subtask extends Task {

    private Integer epicId;

    public Subtask(String title, String description,
                   int epicId,
                   Long duration, LocalDateTime startTime) {
        super(title, description, Status.NEW, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return super.toString()
                + "; epicId " + getEpicId();
    }
}
