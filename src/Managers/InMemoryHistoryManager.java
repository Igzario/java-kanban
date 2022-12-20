package Managers;
import Tasks.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private   final List<Task> history = new LinkedList<>();
    private final int HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (task!=null) {
            if (history.size() == HISTORY_SIZE) {
                history.remove(0);
            }
            history.add(task);
        }
        else {
            System.out.println("Не добавлено");}
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nИстория запросов:");
                for (Task task : history)
        {
            result.append("\nНазвание: "+ task.name);
        }
        return result.toString();
    }
}
