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
}
