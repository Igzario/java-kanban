package Tasks;

public class Task {

    public int id;
    public String name;
    public String description;
    public Status status;

    public Task(int id, String name, String discription) {
        this.id = id;
        this.name = name;
        this.description = discription;
        this.status = Status.NEW;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nID - ");
        result.append(id);
        result.append(", Название - ");
        result.append(name);
        result.append(", Описание  - ");
        result.append(description);
        result.append(", Статус - ");
        result.append(status);
        return result.toString();
    }
}
