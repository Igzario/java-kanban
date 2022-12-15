public class Managers<T extends TaskManager> {

    public T taskManager;

    public TaskManager getDefault() {
        return taskManager;
    }
}
