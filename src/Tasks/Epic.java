import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> epicSubTasksList;

    public Epic(int id, String name, String opisanie) {
        super(id, name, opisanie);
        epicSubTasksList = new ArrayList<>();
    }

    public void refreshStatus() {
        int countNew = 0;
        int countDone = 0;
        int countProgress = 0;

        for (Subtask o : this.epicSubTasksList) {
            if (o.status==Status.NEW) {
                countNew++;
            }
            if (o.status==Status.DONE) {
                countDone++;
            }
            if (o.status==Status.IN_PROGRESS) {
                countProgress++;
            }
        }
        if (countDone == 0 && countProgress == 0 && countNew > 0) {
            this.status = Status.NEW;
        } else if (countDone > 0 && countProgress == 0 && countNew == 0) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }
}
