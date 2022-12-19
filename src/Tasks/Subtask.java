public class Subtask extends Task{

    protected int idEpic ;

    public Subtask(int id, String name, String opisanie, int epic) {
        super(id, name, opisanie);
        this.idEpic = epic;
    }
}
