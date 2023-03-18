package Tasks;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

public class Epic extends Task {
    private ArrayList<Subtask> epicSubTasksList;
    private LocalDateTime endTime;
    private Duration duration;

    public Epic(int id, String name, String discription, Status status, LocalDateTime startTime, Duration duration) {
        super(id, name, discription, status, startTime, duration);
        this.epicSubTasksList = new ArrayList<>();
    }

    public Epic(String name, String discription, Status status, LocalDateTime startTime, Duration duration) {
        super(name, discription, status, startTime, duration);
        this.epicSubTasksList = new ArrayList<>();
    }

    public Epic(int id, String name, String discription, Status status) {
        super(id, name, discription, status);
        this.epicSubTasksList = new ArrayList<>();
    }

    public Epic(String name, String discription, Status status) {
        super(name, discription, status);
        this.epicSubTasksList = new ArrayList<>();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void refreshDuration(Duration duration) {
        if (this.getDuration()==null)
            this.setDuration(duration);
        else
            setDuration(this.getDuration().plus(duration));
    }

    public void refreshTime(){
        epicSubTasksList.sort(Comparator.comparing(Task::getStartTime));
        if (!epicSubTasksList.isEmpty()) {
            setStartTime(epicSubTasksList.get(0).getStartTime());
            endTime = epicSubTasksList.get(epicSubTasksList.size() - 1).getEndTime();
            epicSubTasksList.forEach(subtask -> refreshDuration(subtask.getDuration()));
        }
        else {
            this.endTime = LocalDateTime.MAX;
            this.duration = Duration.between(LocalDateTime.now(), endTime);
        }

    }
    public ArrayList<Subtask> getEpicSubTasksList() {
        return epicSubTasksList;
    }

    public void setEpicSubTasksList(ArrayList<Subtask> epicSubTasksList) {
        this.epicSubTasksList = epicSubTasksList;
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
