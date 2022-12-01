import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> podZadachi;

    public Epic(int id, String name, String opisanie) {
        super(id, name, opisanie);
        podZadachi = new ArrayList<>();
    }

    public void refreshStatus() {
        int schetNew = 0;
        int schetDone = 0;
        int schetProgress = 0;

        for (Subtask o : this.podZadachi) {
            if (o.status==Status.NEW) {
                schetNew++;
            }
            if (o.status==Status.DONE) {
                schetDone++;
            }
            if (o.status==Status.IN_PROGRESS) {
                schetProgress++;
            }
        }
        if (schetDone == 0 && schetProgress == 0 && schetNew > 0) {
            this.status = Status.NEW;
        } else if (schetDone > 0 && schetProgress == 0 && schetNew == 0) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }
}
