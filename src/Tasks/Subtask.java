package Tasks;

public class Subtask extends Task {

    private int idEpic ;

    public int getIdEpic() {
        return idEpic;
    }

    public Subtask(int id, String name, String opisanie, int epic) {
        super(id, name, opisanie);
        this.idEpic = epic;
    }

    public Subtask(int id, String name, String opisanie, Status status , int epic) {
        super(id, name, opisanie, status);
        this.idEpic = epic;
    }
}
