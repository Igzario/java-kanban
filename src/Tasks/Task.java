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

    public Task(int id, String name, String discription, Status status) {
        this.id = id;
        this.name = name;
        this.description = discription;
        this.status = status;
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

    public String getDescription() {
        return description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nID - ")
                .append(id)
                .append(", Название - ")
                .append(name)
                .append(", Описание  - ")
                .append(description)
                .append(", Статус - ")
                .append(status);
        return result.toString();
    }
}
