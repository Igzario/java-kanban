import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import managers.*;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import exeptions.CheckTaskTimeException;
import managers.Managers;

import java.util.LinkedList;

import static tasks.Status.DONE;
import static tasks.Status.NEW;
import static org.junit.jupiter.api.Assertions.assertThrows;


// в эпике переделал EndTime

// При отправке запроса через внешний http-клиент получаю статус 200 (ОК), но тело ответа пустое.  - это поправил

// Этот метод и следующие не используются? Так зачем они? - для проверки, что бы проверять, что ничего не сбилось.
// они используются, в конце когда программа будет полностью готова, я их удалю.

// Этот тест, testSaveAndLoadClearTasks() и testSaveAndLoadClearHistory() падают с ошибкой  - исправил

//Этот и следующие два теста падают с ошибкой
//java.net.BindException: Address already in use: bind - исправил
public class Main {
    private static HttpTaskManager taskManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getHttpTaskManager();
        testKVServer();
        kvServer.stop();
    }

    public static void testKVServer2() throws IOException, InterruptedException {

        taskManager = Managers.getHttpTaskManager();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

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
        System.out.println("=============BEFORE-LOAD==============");
        System.out.println(taskManager);
        System.out.println("=============HISTORY-BEFORE-LOAD==============");
        System.out.println(taskManager.getHistory());
        System.out.println("===========================");
        HttpTaskManager httpTaskManagerAfterLoad = taskManager.load();
        System.out.println("===========================");
        System.out.println("=============AFTER-LOAD==============");
        System.out.println(httpTaskManagerAfterLoad);
        System.out.println("=============HISTORY-AFTER-LOAD==============");
        System.out.println(httpTaskManagerAfterLoad.getHistory());
    }

    public static void testKVServer() throws IOException, InterruptedException {

        taskManager = Managers.getHttpTaskManager();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());

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
        System.out.println("=============BEFORE-LOAD==============");
        System.out.println(taskManager);
        System.out.println("=============HISTORY-BEFORE-LOAD==============");
        System.out.println(taskManager.getHistory());
        System.out.println("===========================");
        HttpTaskManager httpTaskManagerAfterLoad = taskManager.load();
        System.out.println("===========================");
        System.out.println("=============AFTER-LOAD==============");
        System.out.println(httpTaskManagerAfterLoad);
        System.out.println("=============HISTORY-AFTER-LOAD==============");
        System.out.println(httpTaskManagerAfterLoad.getHistory());
    }

    //Адаптер для Duration в минутах
    protected static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            String str = jsonReader.nextString();
            return Duration.ofMinutes(Long.parseLong(str));
        }
    }

    //Адаптер для LocalDateTime
    protected static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        private static final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
            jsonWriter.value(localDate.format(formatterWriter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        }

    }

    // таски пересекаются по времени
    public static void testPeresechenieTime() throws IOException, InterruptedException {
        taskManager = Managers.getHttpTaskManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 21, 0);
        Duration duration1 = Duration.ofMinutes(60);
        LocalDateTime localDateTime2 = LocalDateTime.of(2023, 9, 4, 22, 30);
        Duration duration2 = Duration.ofMinutes(60);
        LocalDateTime localDateTime3 = LocalDateTime.of(2023, 9, 4, 22, 5);
        Duration duration3 = Duration.ofMinutes(60);

        Task task1 = new Task("Test addNewTask1", "Test addNewTask description2", NEW, localDateTime1, duration1);
        taskManager.newTask(task1);

        Task task2 = new Task("Test addNewTask2", "Test addNewTask description2", NEW, localDateTime2, duration2);
        taskManager.newTask(task2);

        Task task3 = new Task("Test addNewTask3", "Test addNewTask description3", NEW, localDateTime3, duration3);

        //-------------------------------------------------------------------------
        assertThrows(CheckTaskTimeException.class, () -> {
            taskManager.newTask(task3);
        });
        //-------------------------------------------------------------------------
        // при добавлении вылетает исключение (код из теста)
        try {
            taskManager.newTask(task3);
        } catch (CheckTaskTimeException e) {
            System.out.println(e.getMessage());
        }
        //-------------------------------------------------------------------------
        //меняем время, что бы не было пересечений
        task2.setStartTime(LocalDateTime.of(2023, 9, 4, 19, 0));
        taskManager.newTask(task3);
        System.out.println("\n\nЗадача добавлена:" + taskManager.searchTaskForId(task3.getId()));
        System.out.println("\n____________________________________________________________________________");
        System.out.println("\n____________________________________________________________________________");

    }

    public static void testEpicTime() throws IOException, InterruptedException {
        taskManager = Managers.getHttpTaskManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 18, 0);
        Duration duration1 = Duration.ofMinutes(60);
        LocalDateTime localDateTime2 = LocalDateTime.of(2023, 9, 4, 12, 0);
        Duration duration2 = Duration.ofMinutes(60);
        LocalDateTime localDateTime3 = LocalDateTime.of(2023, 9, 4, 10, 0);
        Duration duration3 = Duration.ofMinutes(60);
        LocalDateTime localDateTime4 = LocalDateTime.of(2023, 9, 4, 16, 0);
        Duration duration4 = Duration.ofMinutes(60);

        Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description2", NEW);
        int idEpic = taskManager.newEpic(epic1);
        Subtask subtask1 = new Subtask("Test addNewSubTask1", "Test addNewSubTask description1", NEW,
                localDateTime1, duration1, idEpic);
        taskManager.newSubTask(subtask1);
        Subtask subtask2 = new Subtask("Test addNewSubTask2", "Test addNewSubTask description2", NEW,
                localDateTime2, duration2, idEpic);
        taskManager.newSubTask(subtask2);
        Subtask subtask3 = new Subtask("Test addNewSubTask3", "Test addNewSubTask description3", NEW,
                localDateTime3, duration3, idEpic);
        taskManager.newSubTask(subtask3);
        Subtask subtask4 = new Subtask("Test addNewSubTask4", "Test addNewSubTask description4", NEW,
                localDateTime4, duration4, idEpic);
        taskManager.newSubTask(subtask4);

        Duration durationAllSubtask = duration1.plus(duration2).plus(duration3).plus(duration4);
        System.out.println("\nПродолжительность всех подзадач в минутах: " + durationAllSubtask.toMinutes());
        System.out.println("Продолжительность эпика в минутах: " + epic1.getDuration().toMinutes());

        System.out.println("\nВремя старта саммой ранней подзадачи :" + localDateTime3);
        System.out.println("StartTime эпика: " + epic1.getStartTime());

        System.out.println("\nВремя старта саммой поздней подзадачи :" + localDateTime1
                + " Продолжительность в минтуха: " + duration1.toMinutes());
        System.out.println("EndTime  эпика: " + epic1.getEndTimeEpic());

        //-------------------------------------------------------------------------
        //меняем время и продолжительность нескольких подзадач
        localDateTime3 = localDateTime3.plusHours(10);
        duration3 = duration3.plusHours(2);
        taskManager.refreshSubTask(new Subtask(subtask3.getId(), "Test addNewSubTask3", "Test addNewSubTask description3", NEW,
                localDateTime3, duration3, idEpic));

        subtask3 = taskManager.getSubtaskHashMap().get(subtask3.getId());
        durationAllSubtask = duration1.plus(duration2).plus(duration3).plus(duration4);

        System.out.println("\n____________________________________________________________________________");

        System.out.println("\nПродолжительность всех подзадач в минутах: " + durationAllSubtask.toMinutes());
        System.out.println("Продолжительность эпика в минутах: " + epic1.getDuration().toMinutes());

        System.out.println("\nВремя старта саммой ранней подзадачи :" + localDateTime2);
        System.out.println("StartTime эпика: " + epic1.getStartTime());

        System.out.println("\nВремя старта саммой поздней подзадачи :" + localDateTime3
                + " Продолжительность в минтуха: " + duration3.toMinutes() + " - (3ч)");
        System.out.println("EndTime  эпика: " + epic1.getEndTimeEpic());
        System.out.println("\n____________________________________________________________________________");
        System.out.println("\n____________________________________________________________________________");
    }

    public static void tesPprintPrioritizedTasks() throws IOException, InterruptedException {
        taskManager = Managers.getHttpTaskManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 1, 0);
        Duration duration1 = Duration.ofMinutes(60);
        LocalDateTime localDateTime2 = LocalDateTime.of(2023, 9, 4, 2, 0);
        Duration duration2 = Duration.ofMinutes(60);
        LocalDateTime localDateTime3 = LocalDateTime.of(2023, 9, 4, 3, 0);
        Duration duration3 = Duration.ofMinutes(60);
        LocalDateTime localDateTime4 = LocalDateTime.of(2023, 9, 4, 4, 0);
        Duration duration4 = Duration.ofMinutes(60);
        LocalDateTime localDateTime5 = LocalDateTime.of(2023, 9, 4, 5, 0);
        Duration duration5 = Duration.ofMinutes(60);
        LocalDateTime localDateTime6 = LocalDateTime.of(2023, 9, 4, 6, 0);
        Duration duration6 = Duration.ofMinutes(60);

        Epic epic1 = new Epic("Epic", "Test addNewEpic description2", NEW);
        int idEpic = taskManager.newEpic(epic1);

        Task task1 = new Task("Task1", "Test addNewTask description2", NEW, localDateTime1, duration1);
        taskManager.newTask(task1);
        Task task2 = new Task("Task2", "Test addNewTask description2", NEW, localDateTime2, duration2);
        taskManager.newTask(task2);
        Task task3 = new Task("Task3", "Test addNewTask description3", NEW, localDateTime3, duration3);
        taskManager.newTask(task3);

        Subtask subtask4 = new Subtask("SubTask4", "Test addNewSubTask description1", NEW,
                localDateTime4, duration4, idEpic);
        taskManager.newSubTask(subtask4);
        Subtask subtask5 = new Subtask("SubTask5", "Test addNewSubTask description2", NEW,
                localDateTime5, duration5, idEpic);
        taskManager.newSubTask(subtask5);
        Subtask subtask6 = new Subtask("SubTask6", "Test addNewSubTask description3", NEW,
                localDateTime6, duration6, idEpic);
        taskManager.newSubTask(subtask6);

        System.out.println("\n____________________________________________________________________________");
        System.out.println("Вывод задач по приоритету в ручную:");
        System.out.println(task1.getName() + ": " + task1.getStartTime());
        System.out.println(task2.getName() + ": " + task2.getStartTime());
        System.out.println(task3.getName() + ": " + task3.getStartTime());
        System.out.println(subtask4.getName() + ": " + subtask4.getStartTime());
        System.out.println(subtask5.getName() + ": " + subtask5.getStartTime());
        System.out.println(subtask6.getName() + ": " + subtask6.getStartTime());

        System.out.println("\nВывод задач по приоритету через метод:");
        LinkedList<Task> list = taskManager.getPrioritizedTasks();
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.getName() + ": " + task.getStartTime());
        }

        //-------------------------------------------------------------------------
        //меняем startTime нескольких подзадач
        localDateTime3 = localDateTime3.plusHours(5);
        task3 = new Task(task3.getId(), "Task3", "Test addNewTask description3", NEW, localDateTime3, duration3);
        taskManager.refreshTask(task3);

        localDateTime5 = localDateTime5.minusHours(5);
        subtask5 = new Subtask(subtask5.getId(), "Task5", "Test addNewTask description3", NEW, localDateTime5, duration5, idEpic);
        taskManager.refreshSubTask(subtask5);

        System.out.println("\n____________________________________________________________________________");
        System.out.println("Вывод задач по приоритету в ручную:");
        System.out.println(subtask5.getName() + ": " + subtask5.getStartTime());
        System.out.println(task1.getName() + ": " + task1.getStartTime());
        System.out.println(task2.getName() + ": " + task2.getStartTime());
        System.out.println(subtask4.getName() + ": " + subtask4.getStartTime());
        System.out.println(subtask6.getName() + ": " + subtask6.getStartTime());
        System.out.println(task3.getName() + ": " + task3.getStartTime());

        System.out.println("\nВывод задач по приоритету через метод:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.getName() + ": " + task.getStartTime());
        }


        //-------------------------------------------------------------------------
        //меняем статус task2 на DONE, задачадолжна исчезнуть из списка

        task2 = new Task(task2.getId(), "Task2", "Test addNewTask description3", DONE, localDateTime2, duration2);
        taskManager.refreshTask(task2);

        localDateTime4 = null;
        subtask4 = new Subtask(subtask4.getId(), "SubTask4", "Test addNewSubTask description1", NEW,
                localDateTime4, duration4, idEpic);
        taskManager.refreshSubTask(subtask4);


        System.out.println("\n____________________________________________________________________________");
        System.out.println("Вывод задач по приоритету в ручную:");
        System.out.println(subtask5.getName() + ": " + subtask5.getStartTime());
        System.out.println(task1.getName() + ": " + task1.getStartTime());
        System.out.println(subtask6.getName() + ": " + subtask6.getStartTime());
        System.out.println(task3.getName() + ": " + task3.getStartTime());
        System.out.println(subtask4.getName() + ": " + subtask4.getStartTime());

        System.out.println("\nВывод задач по приоритету через метод:");
        for (Task task : taskManager.getPrioritizedTasks()) {
            System.out.println(task.getName() + ": " + task.getStartTime());
        }

    }
}