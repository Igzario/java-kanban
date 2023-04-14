import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import managers.HttpTaskManager;
import managers.KVServer;
import managers.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.NEW;

class HttpTaskManagerTest {
    HttpTaskManager taskManager;

    @SneakyThrows
    @Test
    public void saveTestAndLoad() {
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            taskManager = Managers.getHttpTaskManager();
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(LocalDateTime.class, new Main.LocalDateAdapter());
            gsonBuilder.registerTypeAdapter(Duration.class, new Main.DurationAdapter());

            LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 21, 0);
            Duration duration1 = Duration.ofMinutes(60);
            LocalDateTime localDateTime2 = LocalDateTime.of(2023, 8, 4, 22, 30);
            Duration duration2 = Duration.ofMinutes(60);
            LocalDateTime localDateTime3 = LocalDateTime.of(2023, 10, 4, 22, 5);
            Duration duration3 = Duration.ofMinutes(60);
            LocalDateTime localDateTime4 = LocalDateTime.of(2023, 9, 2, 21, 0);
            Duration duration4 = Duration.ofMinutes(60);
            LocalDateTime localDateTime5 = LocalDateTime.of(2023, 8, 3, 22, 30);
            Duration duration5 = Duration.ofMinutes(60);
            LocalDateTime localDateTime6 = LocalDateTime.of(2023, 10, 9, 22, 5);
            Duration duration6 = Duration.ofMinutes(60);
            LocalDateTime localDateTime7 = LocalDateTime.of(2023, 10, 1, 22, 5);
            Duration duration7 = Duration.ofMinutes(60);

            Task task1 = new Task("Test addNewTask1", "Test addNewTask description2", NEW, localDateTime1, duration1);
            Task task2 = new Task("Test addNewTask2", "Test addNewTask description2", NEW, localDateTime2, duration2);
            Task task3 = new Task("Test addNewTask3", "Test addNewTask description3", NEW, localDateTime3, duration3);
            Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description2", NEW);
            int idEpic = taskManager.newEpic(epic1);
            Subtask subtask1 = new Subtask("Test addNewSubTask1", "Test addNewSubTask description1", NEW,
                    localDateTime4, duration4, idEpic);
            taskManager.newSubTask(subtask1);
            Subtask subtask2 = new Subtask("Test addNewSubTask2", "Test addNewSubTask description2", NEW,
                    localDateTime5, duration5, idEpic);
            taskManager.newSubTask(subtask2);
            Subtask subtask3 = new Subtask("Test addNewSubTask3", "Test addNewSubTask description3", NEW,
                    localDateTime6, duration6, idEpic);
            taskManager.newSubTask(subtask3);
            Subtask subtask4 = new Subtask("Test addNewSubTask4", "Test addNewSubTask description4", NEW,
                    localDateTime7, duration7, idEpic);
            taskManager.newSubTask(subtask4);

            taskManager.newTask(task1);
            taskManager.newTask(task2);
            taskManager.newTask(task3);
            taskManager.searchTaskForId(7);
            taskManager.searchTaskForId(8);
            taskManager.searchTaskForId(6);
            taskManager.searchEpicForId(1);
            taskManager.searchSubtaskForId(2);
            taskManager.searchSubtaskForId(4);
            taskManager.searchSubtaskForId(3);

            HttpTaskManager httpTaskManagerAfterLoad = taskManager.load();

            assertEquals(taskManager, httpTaskManagerAfterLoad);
            kvServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}