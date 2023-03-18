package Tasks;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;
    private LocalDateTime startTime;
    private Duration duration;


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

    public Task( String name, String discription, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = discription;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

//
//    public Task(int id, String name, String discription, Status status) {
//        this.id = id;
//        this.name = name;
//        this.description = discription;
//        this.status = status;
//
//    }
//    public Task( String name, String discription, Status status) {
//        this.name = name;
//        this.description = discription;
//        this.status = status;
//    }

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
