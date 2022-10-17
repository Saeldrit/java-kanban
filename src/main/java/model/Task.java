package model;

import lombok.*;
import model.status.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"description", "duration"})
@Builder
@AllArgsConstructor
public class Task {
    private Integer id;
    private String title;
    private String description;
    private Status status;
    private Long duration;
    private LocalDateTime startTime;

    public Task(String title, String description,
                Status status,
                Long duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    protected String converterDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm"));
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + ": id - " + getId()
                + "; title - " + getTitle()
                + "; status - " + getStatus()
                + "; startTime - " + converterDateTimeToString(getStartTime())
                + "; endTime - " + converterDateTimeToString(getEndTime());
    }
}
