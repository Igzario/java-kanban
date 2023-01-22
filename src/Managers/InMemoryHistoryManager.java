package Managers;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public Node<Task> tail;
    private int size = 0;

    private HashMap<Integer, Node> hashHistory = new HashMap<>();

    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<Task>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            tail = newNode;
        } else
            oldTail.next = newNode;
            size++;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();

        if (tail.data != null) {
            Node<Task> check = tail;

            for (int i = 0; i < size; i++) {
                if (check != null && check.data != null) {
                    tasks.add(check.data);
                    check = check.prev;
                }
            }
        }
        return tasks;
    }

    public void removeNode(Node node) {
        Node<Task> check = tail;
        for (int i = 0; i < size; i++) {
            if (check == node && check.prev == null) {
                check.data = null;
                size--;
                check.next.prev = null;
            } else if (check == node && check.next == null) {
                check.data = null;
                size--;
                tail = check.prev;
            } else if (check == node) {
                check.prev.next = check.next;
                check.data = null;
                check.next.prev = check.prev;
                size--;
            } else
                check = check.prev;
        }
    }


    @Override
    public void add(Task task) {
        if (task != null) {

            if (hashHistory.containsKey(task.id)) {
                removeNode(hashHistory.get(task.id));
                linkLast(task);
            } else {
                linkLast(task);
                hashHistory.put(task.id, tail);
            }

        } else {
            System.out.println("Не добавлено");
        }
    }

    @Override
    public void remove(int id) {
        removeNode(hashHistory.get(id));
        hashHistory.remove(id);

    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\nИстория запросов:");
        for (Task task : getTasks()) {
            result.append("\nНазвание: " + task.name);
        }
        return result.toString();
    }
}
