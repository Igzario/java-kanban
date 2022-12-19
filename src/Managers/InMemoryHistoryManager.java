package Managers;

import Tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    protected final ArrayList<Task> history = new ArrayList<>();
    protected final int HISTORY_SIZE = 10;

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
    public ArrayList<Task> getHistory() {
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
