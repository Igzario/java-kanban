package tasks;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class Epic extends Task {
    @Getter
    @Setter
    private ArrayList<Subtask> epicSubTasksList = new ArrayList<>();

    public Epic(int id, String name, String description, Status status) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setStatus(status);
        this.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        this.setEndTime(LocalDateTime.of(999999, 1, 1, 1, 1));
        this.setDuration(Duration.ZERO);
    }

    public Epic(String name, String discription, Status status) {
        this.setName(name);
        this.setDescription(discription);
        this.setStatus(status);
        this.epicSubTasksList = new ArrayList<>();
        this.setStartTime(LocalDateTime.of(1, 1, 1, 1, 1));
        this.setEndTime(LocalDateTime.of(999999, 1, 1, 1, 1));
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
                    this.setEndTime(LocalDateTime.of(999999, 1, 1, 1, 1));
                }
            }
            if (testSubTask.getEndTime().equals(LocalDateTime.of(999999, 1, 1, 1, 1))) {
                this.setEndTime(epicSubTasksList.get(epicSubTasksList.size() - 1).getEndTime());
            } else {
                this.setEndTime(testSubTask.getEndTime());
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nid - ")
                .append(this.getId())
                .append(", name - ")
                .append(this.getName())
                .append(", description  - ")
                .append(this.getDescription())
                .append(", status - ")
                .append(this.getStatus())
                .append(", startTime - ")
                .append(this.getStartTime())
                .append(", duration - ")
                .append(this.getDuration())
                .append(", endTime - ")
                .append(this.getEndTime());
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        boolean ok = false;
        if (this.getId() == epic.getId() && this.getName().equals(epic.getName())
                && this.getDescription().equals(epic.getDescription())
                && this.getStatus() == epic.getStatus()) {
            if (epicSubTasksList.size() == 0 && epic.epicSubTasksList.size() == 0) {
                return true;
            } else if (epicSubTasksList.size() == epic.epicSubTasksList.size()) {
                for (Subtask subtask : epicSubTasksList) {
                    for (Subtask subtaskO : epic.getEpicSubTasksList()) {
                        if (subtask.equals(subtaskO)) {
                            ok = true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return ok;
    }
}