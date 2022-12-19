package Tasks;

public class Task {

    public int id;
    public String name;
    public String discription;
    public Status status;

    public Task(int id, String name, String opisanie) {
        this.id = id;
        this.name = name;
        this.discription = opisanie;
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nID - " + id + ", Название - " + name
                + ", Описание  - " + discription
                + ", Статус - " + status);
        return result.toString();
    }
}
