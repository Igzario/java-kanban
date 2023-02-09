package Managers;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;
    private int size = 0;

    private final HashMap<Integer, Node> hashHistory = new HashMap<>();

    public void linkLast(Task task) {
        final Node oldTail = head;
        final Node newNode = new Node(null, task, oldTail);
        head = newNode;
        if (oldTail == null) {
            head = newNode;
        } else
            oldTail.prev = newNode;
        size++;
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        if (head !=null && head.data != null) {
            Node check = head;
            for (int i = 0; i < size; i++) {
                if (check.data != null && check.next != null) {
                    check = check.next;
                }
            }
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
        if (node != null) {
            Node check = head;
            for (int i = 0; i < size; i++) {
                if (check == node && check.next == null && check.prev == null) {
                    check.data = null;
                    size--;
                    return;
                } else if (check == node && check.next == null) {
                    check.data = null;
                    size--;
                    check.prev.next = null;
                    return;
                } else if (check == node && check.prev == null) {
                    check.data = null;
                    size--;
                    head = check.next;
                    return;
                } else if (check == node) {
                    check.next.prev = check.prev;
                    check.data = null;
                    check.prev.next = check.next;
                    size--;
                    return;
                } else
                    check = check.next;
            }
        } else
            System.out.println("Удаление Node не возможно");
    }


    @Override
    public void add(Task task) {
        if (task != null) {
            if (hashHistory.containsKey(task.getId())) {
                removeNode(hashHistory.get(task.getId()));
                hashHistory.remove(task.getId());
                linkLast(task);
                hashHistory.put(task.getId(), head);
            } else {
                linkLast(task);
                hashHistory.put(task.getId(), head);
            }
        } else {
            System.out.println("Не добавлено");
        }
    }

    @Override
    public void remove(int id) {
        removeNode(hashHistory.get(id));
        hashHistory.remove(id);
        if (size==0)
        {
            head =null;
        }

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
            result.append("\nНазвание: ")
                    .append(task.getName());
        }
        return result.toString();
    }
}
