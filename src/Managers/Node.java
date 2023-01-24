package Managers;

class Node<Task> {
    protected Task data;
    protected Node<Task> next;
    protected Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}
