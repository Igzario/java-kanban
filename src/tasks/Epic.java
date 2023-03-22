package tasks;
import lombok.Getter;
import lombok.Setter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class Epic extends Task {
    @Getter
    @Setter
    private ArrayList<Subtask> epicSubTasksList;
    @Getter
    @Setter
    private LocalDateTime endTime;

    public Epic(int id, String name, String discription, Status status) {
        this.setId(id);
        this.setName(name);
        this.setDescription(discription);
        this.setStatus(status);
        this.epicSubTasksList = new ArrayList<>();
        this.setStartTime(LocalDateTime.MIN);
        this.endTime = LocalDateTime.MAX;
        this.setDuration(Duration.ZERO);
    }

    public Epic(String name, String discription, Status status) {
        this.setName(name);
        this.setDescription(discription);
        this.setStatus(status);
        this.epicSubTasksList = new ArrayList<>();
        this.setStartTime(LocalDateTime.MIN);
        this.endTime = LocalDateTime.MAX;
        this.setDuration(Duration.ZERO);
    }

    private void refreshDuration(Duration duration) {
        if (this.getDuration() == Duration.ZERO) {
            this.setDuration(duration);
        } else {
            this.setDuration(getDuration().plus(duration));
        }
    }

    public void refreshTime() {
        Comparator comparator = (o1, o2) -> {
            if (((Task) o1).getStartTime() == null)
                return 1;
            else {
                if (((Task) o1).getStartTime().isAfter(((Task) o2).getStartTime())) {
                    return 1;
                } else if (((Task) o1).getStartTime().isBefore(((Task) o2).getStartTime())) {
                    return -1;
                } else
                    return 0;
            }
        };
        epicSubTasksList.sort(comparator);
        if (!epicSubTasksList.isEmpty()) {
            setStartTime(epicSubTasksList.get(0).getStartTime());
            Subtask testSubTask = null;
            for (Subtask subtask : epicSubTasksList) {
                if (subtask.getStartTime() != null) {
                    testSubTask = subtask;
                } else {
                    endTime = LocalDateTime.MAX;
                }
            }
            if (testSubTask.getEndTime() == LocalDateTime.MAX) {
                endTime = epicSubTasksList.get(epicSubTasksList.size() - 1).getEndTime();
            } else {
                endTime = testSubTask.getEndTime();
            }
            this.setDuration(Duration.ZERO);
            epicSubTasksList.forEach(subtask -> refreshDuration(subtask.getDuration()));
        }
    }

    public void refreshStatusAndTime() {
        refreshTime();
        int countNew = 0;
        int countDone = 0;
        int countProgress = 0;
        for (Subtask o : this.epicSubTasksList) {
            if (o.getStatus() == Status.NEW) {
                countNew++;
            }
            if (o.getStatus() == Status.DONE) {
                countDone++;
            }
            if (o.getStatus() == Status.IN_PROGRESS) {
                countProgress++;
            }
        }
        if (countDone == 0 && countProgress == 0 && countNew > 0) {
            this.setStatus(Status.NEW);
        } else if (countDone > 0 && countProgress == 0 && countNew == 0) {
            this.setStatus(Status.DONE);
        } else {
            this.setStatus(Status.IN_PROGRESS);
        }
    }
}
