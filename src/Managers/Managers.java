package Managers;

import java.nio.file.Path;

public class Managers {

    public static FileBackedTasksManager getDefault(Path path) {
        return new FileBackedTasksManager(path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }



}
