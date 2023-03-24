package managers;

import java.nio.file.Paths;

public class Managers {

    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager(Paths.get("resources\\BackUpTasksManager.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
