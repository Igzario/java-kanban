package Managers;
import Tasks.Task;

public class Node {
    protected Task data;
    protected Node next;
    protected Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
