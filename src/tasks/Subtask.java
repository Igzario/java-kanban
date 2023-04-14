package tasks;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    @Getter
    @Setter
    private int idEpic;

    public Subtask(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration, int idEpic) {
        super(id, name, description, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String description, Status status, LocalDateTime startTime, Duration duration, int idEpic) {
        super(name, description, status, startTime, duration);
        this.idEpic = idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return idEpic == subtask.idEpic;
    }
}
