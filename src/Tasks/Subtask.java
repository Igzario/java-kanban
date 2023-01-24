package Tasks;

public class Subtask extends Task {

    public int idEpic ;

    public Subtask(int id, String name, String opisanie, int epic) {
        super(id, name, opisanie);
        this.idEpic = epic;
    }
}
