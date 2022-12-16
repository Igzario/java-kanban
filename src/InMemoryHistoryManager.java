import java.util.ArrayList;

public class InMemoryHistoryManager implements  HistoryManager{

    ArrayList<Task> history = new ArrayList<>();
    final int historySize = 10;

    @Override
    public void add(Task task) {
        if (history.size() == historySize) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }

    @Override
    public String toString() {
        String result = "\nИстория запросов:";
        for (Task task : history)
        {
            result += "\nНазвание: "+ task.name;
        }
        return result;
    }
}
