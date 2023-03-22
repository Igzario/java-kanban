package tasks;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
@Getter @Setter
    private int idEpic ;

    public Subtask(int id, String name, String discription, Status status, LocalDateTime startTime, Duration duration, int idEpic) {
        super(id, name, discription, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String discription, Status status, LocalDateTime startTime, Duration duration, int idEpic) {
        super(name, discription, status, startTime, duration);
        this.idEpic = idEpic;
    }
}
