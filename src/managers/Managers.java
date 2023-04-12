package managers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public class Managers {

    public static FileBackedTasksManager getDefault() {
        return new FileBackedTasksManager(Paths.get("resources\\BackUpTasksManager.csv"));
    }

    public static HttpTaskManager getHttpTaskManager() throws IOException, InterruptedException {
        return new HttpTaskManager(new URL("http://localhost:8078"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
