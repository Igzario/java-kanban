package Managers;

public class Managers<T extends TaskManager> {

    public TaskManager getDefault() {
        return null;
    }

    public HistoryManager getDefaultHistory(T taskManager) {
        if (taskManager instanceof InMemoryTaskManager) {
            HistoryManager historyManager = new InMemoryHistoryManager();
            return historyManager;
        }
        return null;
    }
}
