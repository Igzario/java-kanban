package Tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int idEpic ;

//    public Subtask(int id, String name, String opisanie, Status status, int epic) {
//        super(id,name, opisanie,status);
//        this.idEpic = epic;
//
//    }
//
//    public Subtask(String name, String discription, Status status, int idEpic) {
//        super(name, discription, status);
//        this.idEpic = idEpic;
//    }

    public Subtask(int id, String name, String discription, Status status, LocalDateTime startTime, Duration duration, int idEpic) {
        super(id, name, discription, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public Subtask(String name, String discription, Status status, LocalDateTime startTime, Duration duration, int idEpic) {
        super(name, discription, status, startTime, duration);
        this.idEpic = idEpic;
    }

    public int getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(int idEpic) {
        this.idEpic = idEpic;
    }
}
