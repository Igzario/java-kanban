import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import managers.FileBackedTasksManager;
import managers.HttpTaskServer;
import managers.Managers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.NEW;

class HttpTaskServerTest {
    HttpTaskServer server;
    Gson gson;
    GsonBuilder gsonBuilder;
    HttpClient client;

    @BeforeEach
    public void startServer() throws IOException {
        server = new HttpTaskServer();
        server.srart();
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new HttpTaskServer.LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new HttpTaskServer.DurationAdapter());
        gson = gsonBuilder.create();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void testGetTasks() {
        try {
            FileBackedTasksManager fileBackedTasksManager = Managers.getDefault();
            LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 21, 0);
            Duration duration1 = Duration.ofMinutes(60);
            LocalDateTime localDateTime2 = LocalDateTime.of(2023, 8, 4, 22, 30);
            Duration duration2 = Duration.ofMinutes(60);
            LocalDateTime localDateTime4 = LocalDateTime.of(2023, 9, 2, 21, 0);
            Duration duration4 = Duration.ofMinutes(60);
            LocalDateTime localDateTime5 = LocalDateTime.of(2023, 8, 3, 22, 30);
            Duration duration5 = Duration.ofMinutes(60);

            Task task1 = new Task("Test addNewTask1", "Test addNewTask description2", NEW, localDateTime1, duration1);
            Task task2 = new Task("Test addNewTask2", "Test addNewTask description2", NEW, localDateTime2, duration2);
            Epic epic1 = new Epic("Test addNewEpic1", "Test addNewEpic description2", NEW);

            fileBackedTasksManager.newTask(task1);
            fileBackedTasksManager.newTask(task2);
            int idEpic = fileBackedTasksManager.newEpic(epic1);

            Subtask subtask1 = new Subtask("Test addNewSubTask1", "Test addNewSubTask description1", NEW,
                    localDateTime4, duration4, idEpic);

            Subtask subtask2 = new Subtask("Test addNewSubTask2", "Test addNewSubTask description2", NEW,
                    localDateTime5, duration5, idEpic);
            fileBackedTasksManager.newSubTask(subtask1);
            fileBackedTasksManager.newSubTask(subtask2);

            server.setManager(fileBackedTasksManager);


            URI url = URI.create("http://localhost:8080/tasks/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            String[] responseTasks = response.body().split("\n");

            ArrayList<LinkedTreeMap> tasks = gson.fromJson(responseTasks[0], ArrayList.class);
            ArrayList<LinkedTreeMap> epics = gson.fromJson(responseTasks[1], ArrayList.class);
            ArrayList<LinkedTreeMap> subtasks = gson.fromJson(responseTasks[2], ArrayList.class);

            for (LinkedTreeMap mapTasks : tasks) {
                int id = (int) ((double) mapTasks.get("id"));
                String name = (String) mapTasks.get("name");
                String description = (String) mapTasks.get("description");
                Status status = Status.NEW;
                String statusStr = (String) mapTasks.get("status");
                switch (statusStr) {
                    case "IN_PROGRESS":
                        status = Status.IN_PROGRESS;
                    case "DONE":
                        status = Status.DONE;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                LocalDateTime startTime = LocalDateTime.parse((String) mapTasks.get("startTime"), formatter);
                Duration duration = Duration.ofMinutes((long) ((double) mapTasks.get("duration")));

                Task task = new Task(id, name, description, status, startTime, duration);

                assertEquals(task, server.getManager().searchTaskForId(id));
            }
            for (LinkedTreeMap mapEpics : epics) {
                ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) mapEpics.get("epicSubTasksList");
                int id = (int) ((double) mapEpics.get("id"));
                String name = (String) mapEpics.get("name");
                String description = (String) mapEpics.get("description");
                Status status = Status.NEW;
                String statusStr = (String) mapEpics.get("status");
                switch (statusStr) {
                    case "IN_PROGRESS":
                        status = Status.IN_PROGRESS;
                    case "DONE":
                        status = Status.DONE;
                }

                Epic epic = new Epic(id, name, description, status);

                for (LinkedTreeMap mapSubtasks : list) {
                    int idSubtask = (int) ((double) mapSubtasks.get("id"));
                    int idEpSubtask = (int) ((double) mapSubtasks.get("idEpic"));
                    String nameSubtask = (String) mapSubtasks.get("name");
                    String descriptionSubtask = (String) mapSubtasks.get("description");
                    Status statusSubtask = Status.NEW;
                    String statusStrSubtask = (String) mapSubtasks.get("status");
                    switch (statusStr) {
                        case "IN_PROGRESS":
                            status = Status.IN_PROGRESS;
                        case "DONE":
                            status = Status.DONE;
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                    LocalDateTime startTimeSubtask = LocalDateTime.parse((String) mapSubtasks.get("startTime"), formatter);
                    Duration durationSubtask = Duration.ofMinutes((long) ((double) mapSubtasks.get("duration")));

                    Subtask subTask = new Subtask(idSubtask, nameSubtask, descriptionSubtask, statusSubtask, startTimeSubtask, durationSubtask, idEpSubtask);
                    epic.getEpicSubTasksList().add(subTask);
                    assertEquals(subTask, server.getManager().searchSubtaskForId(idSubtask));
                }
                assertEquals(epic, server.getManager().searchEpicForId(id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }

    @Test
    public void testTaskPostAndGet() throws IOException, InterruptedException {
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 21, 0);
        Duration duration1 = Duration.ofMinutes(60);
        Task task = new Task("Test addNewTask1", "Test addNewTask description2", NEW, localDateTime1, duration1);


        URI urlPost = URI.create("http://localhost:8080/tasks/task");
        String jsonTask = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest requestPost = HttpRequest.newBuilder().uri(urlPost).POST(body).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> responsePost = client.send(requestPost, handler);

        Task savedTask = server.getManager().searchTaskForId(1);
        task.setId(savedTask.getId());

        assertEquals(201, responsePost.statusCode());
        assertEquals("Задача успешно добавлена\nID = " + savedTask.getId(), responsePost.body());
        assertEquals(task, savedTask);

        URI urlGet = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseGet.body());

        Task getTask = gson.fromJson(responseGet.body(), Task.class);
        assertEquals(200, responseGet.statusCode());
        assertEquals(task, getTask);
        server.stop();
    }

    @Test
    public void testSubTaskPostAndGet() throws IOException, InterruptedException {
        testEpicPostAndGet();

        Epic epic = server.getManager().searchEpicForId(1);
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 21, 0);
        Duration duration1 = Duration.ofMinutes(60);
        Subtask subtask = new Subtask("Test addNewSubTask1", "Test addNewSubTask description1", NEW,
                localDateTime1, duration1, epic.getId());

        URI urlPost = URI.create("http://localhost:8080/tasks/subtask");
        String jsonSubtask = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubtask);
        HttpRequest requestPost = HttpRequest.newBuilder().uri(urlPost).POST(body).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> responsePost = client.send(requestPost, handler);

        Task savedSubtask = server.getManager().searchSubtaskForId(2);
        subtask.setId(savedSubtask.getId());

        assertEquals(201, responsePost.statusCode());
        assertEquals("Подзадача успешно добавлена\nID = " + savedSubtask.getId(), responsePost.body());
        assertEquals(subtask, savedSubtask);

        URI urlGet = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseGet.body());

        Task getSubtask = gson.fromJson(responseGet.body(), Subtask.class);
        assertEquals(200, responseGet.statusCode());
        assertEquals(subtask, getSubtask);
        server.stop();
    }

    @Test
    public void testEpicPostAndGet() throws IOException, InterruptedException {
        Epic epic = new Epic("Test addNewEpic1", "Test addNewEpic description2", NEW);

        URI urlPost = URI.create("http://localhost:8080/tasks/epic");
        String jsonEpic = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest requestPost = HttpRequest.newBuilder().uri(urlPost).POST(body).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        HttpResponse<String> responsePost = client.send(requestPost, handler);

        Epic savedEpic = server.getManager().searchEpicForId(1);

        epic.setId(savedEpic.getId());

        assertEquals(201, responsePost.statusCode());
        assertEquals("Эпик успешно добавлен\nID = " + savedEpic.getId(), responsePost.body());
        assertEquals(epic, savedEpic);

        URI urlGet = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest requestGet = HttpRequest.newBuilder().uri(urlGet).GET().build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        System.out.println(responseGet.body());

        Epic getEpic = gson.fromJson(responseGet.body(), Epic.class);
        assertEquals(200, responseGet.statusCode());
        assertEquals(epic, getEpic);
    }
}