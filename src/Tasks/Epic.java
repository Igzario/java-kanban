package Tasks;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> epicSubTasksList;

    public Epic(int id, String name, String opisanie) {
        super(id, name, opisanie);
        epicSubTasksList = new ArrayList<>();
    }
    public Epic(int id, String name, String opisanie, Status status) {
        super(id, name, opisanie, status);
        epicSubTasksList = new ArrayList<>();
    }

    public ArrayList<Subtask> getEpicSubTasksList() {
        return epicSubTasksList;
    }

    public void setEpicSubTasksList(ArrayList<Subtask> epicSubTasksList) {
        this.epicSubTasksList = epicSubTasksList;
    }

    public void refreshStatus() {
        int countNew = 0;
        int countDone = 0;
        int countProgress = 0;

        for (Subtask o : this.epicSubTasksList) {
            if (o.getStatus()== Status.NEW) {
                countNew++;
            }
            if (o.getStatus()==Status.DONE) {
                countDone++;
            }
            if (o.getStatus()==Status.IN_PROGRESS) {
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
