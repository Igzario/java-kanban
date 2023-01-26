package Tasks;

public class Task {

    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(int id, String name, String discription) {
        this.id = id;
        this.name = name;
        this.description = discription;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
