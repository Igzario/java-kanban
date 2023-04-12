package tasks;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    @Getter    @Setter
    private int id;
    @Getter    @Setter
    private String name;
    @Getter    @Setter
    private String description;
    @Getter    @Setter
    private Status status;
    @Getter    @Setter
    private LocalDateTime startTime;
    @Getter    @Setter
    private Duration duration;
    @Getter    @Setter
    private LocalDateTime endTime;


    public Task() {
    }

    public Task(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plus(duration);
    }

    public LocalDateTime getEndTime() {
        if (startTime==null || duration ==null)
        {
            endTime = null;
        }
        else
        {
            endTime = startTime.plus(duration);
        }
        return endTime;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nid - ")
                .append(id)
                .append(", name - ")
                .append(name)
                .append(", description  - ")
                .append(description)
                .append(", status - ")
                .append(status)
                .append(", startTime - ")
                .append(startTime)
                .append(", duration - ")
                .append(duration)
                .append(", endTime - ")
                .append(endTime);
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id
                && Objects.equals(name, task.name)
                & Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(duration, task.duration)
                && Objects.equals(endTime, task.endTime);
    }

}
