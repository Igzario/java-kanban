import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Subtask> podZadachi;

    public Epic(int id, String name, String opisanie) {
        super(id, name, opisanie);
        podZadachi = new ArrayList<>();
    }

    public void refreshStatus() {
        int schetNew = 0;
        int schetDone = 0;
        int schetProgress = 0;

        if (this.podZadachi.isEmpty()) {
            this.status = "NEW";
        }

        for (Subtask o : this.podZadachi) {

            if (o.status.equals("NEW")) {
                schetNew++;
            }
            if (o.status.equals("DONE")) {
                schetDone++;
            }
            if (o.status.equals("IN_PROGRESS")) {
                schetProgress++;
            }
        }

        if (schetDone == 0 && schetProgress == 0 && schetNew > 0) {
            this.status = "NEW";
        } else if (schetDone > 0 && schetProgress == 0 && schetNew == 0) {
            this.status = "DONE";
        } else {
            this.status = "IN_PROGRESS";
        }
    }
}
