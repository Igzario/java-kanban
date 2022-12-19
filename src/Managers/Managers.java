public class Managers<T extends TaskManager, Y extends HistoryManager> {

    T taskManager;
    Y historyManager;

    public Managers(T taskManager, Y historyManager) {
        this.taskManager = taskManager;
        this.historyManager = historyManager;
    }

    public TaskManager getDefault() {
        return taskManager;
    }

    public HistoryManager getDefaultHistory() {
        return historyManager;
    }


}
