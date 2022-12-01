public class Task {

    protected int id;
    protected String name;
    protected String opisanie;
    protected Status status;

    public Task(int id, String name, String opisanie) {
        this.id = id;
        this.name = name;
        this.opisanie = opisanie;
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        return  "ID - " + id +
                ", Название - " + name + '\'' +
                ", Описание  - " + opisanie + '\'' +
                ", Статус - " + status;
    }
}
