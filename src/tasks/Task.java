package tasks;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDateTime;

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

    public Task(int id, String name, String discription, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = discription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String discription, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = discription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
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
        result.append("\nID - ")
                .append(id)
                .append(", Название - ")
                .append(name)
                .append(", Описание  - ")
                .append(description)
                .append(", Статус - ")
                .append(status)
                .append(", Дата старта - ")
                .append(startTime)
                .append(", Продолжительность выполнения - ")
                .append(duration);
        return result.toString();
    }
}
