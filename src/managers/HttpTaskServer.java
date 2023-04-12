package managers;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {

    HttpServer httpServer;
    private static final int PORT = 8080;
    @Getter
    @Setter
    FileBackedTasksManager manager;
    Gson gson;
    GsonBuilder gsonBuilder;

    public HttpTaskServer() throws IOException, InterruptedException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        this.manager = Managers.getDefault();
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
        httpServer.createContext("/tasks", new tasksHandler());
        httpServer.createContext("/tasks/task", new taskHandler());
        httpServer.createContext("/tasks/epic", new epicHandler());
        httpServer.createContext("/tasks/subtask", new subTaskHandler());
    }

    //  /tasks/task
    public class taskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String requestMethod = httpExchange.getRequestMethod();
                String query = httpExchange.getRequestURI().getQuery();
                int id;
                if (query != null) {
                    id = Integer.parseInt(query.substring(query.length() - 1));
                } else {
                    id = 0;
                }
                String response;
                Task task;
                switch (requestMethod) {
                    case "GET": {
                        task = manager.searchTaskForId(id);
                        if (task != null) {
                            response = gson.toJson(task);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Нет задачи с ID = " + id;
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    case "POST": {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                        task = gson.fromJson(body, Task.class);
                        id = manager.newTask(task);
                        response = "Задача успешно добавлена\nID = " + id;
                        httpExchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    case "DELETE": {
                        if (manager.getTaskHashMap().containsKey(id)) {
                            manager.deleteTaskForId(id);
                            response = "Задача c ID " + id + " успешно удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Нет задачи с ID = " + id;
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    default: {
                        System.out.println(requestMethod + " - Не верный метод.");
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
            httpExchange.close();
        }
    }

    public class epicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String requestMethod = httpExchange.getRequestMethod();
                String query = httpExchange.getRequestURI().getQuery();
                int id;
                if (query != null) {
                    id = Integer.parseInt(query.substring(query.length() - 1));
                } else {
                    id = 0;
                }
                String response;
                Epic epic;
                switch (requestMethod) {
                    case "GET": {
                        epic = manager.searchEpicForId(id);
                        if (epic != null) {
                            response = gson.toJson(epic);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Нет эпика' с ID = " + id;
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    case "POST": {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                        epic = gson.fromJson(body, Epic.class);
                        id = manager.newEpic(epic);
                        response = "Эпик успешно добавлен\nID = " + id;
                        httpExchange.sendResponseHeaders(201, 0);
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    case "DELETE": {
                        if (manager.getEpicHashMap().containsKey(id)) {
                            manager.deleteEpicForId(id);
                            response = "Эпик c ID " + id + " успешно удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Нет эпика с ID = " + id;
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    default: {
                        System.out.println(requestMethod + " - Не верный метод.");
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
            httpExchange.close();
        }
    }

    public class subTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String requestMethod = httpExchange.getRequestMethod();
                String query = httpExchange.getRequestURI().getQuery();
                int id;
                if (query != null) {
                    id = Integer.parseInt(query.substring(query.length() - 1));
                } else {
                    id = 0;
                }
                String response;
                Subtask subtask;
                switch (requestMethod) {
                    case "GET": {
                        subtask = (Subtask) manager.searchSubtaskForId(id);
                        if (subtask != null) {
                            response = gson.toJson(subtask);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Нет подзадачи' с ID = " + id;
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    case "POST": {
                        InputStream inputStream = httpExchange.getRequestBody();
                        String body = new String(inputStream.readAllBytes(), Charset.defaultCharset());
                        subtask = gson.fromJson(body, Subtask.class);
                        if (manager.getEpicHashMap().containsKey(subtask.getIdEpic())) {
                            id = manager.newSubTask(subtask);
                            response = "Подзадача успешно добавлена\nID = " + id;
                            httpExchange.sendResponseHeaders(201, 0);
                        } else {
                            response = "Не возможно добавить подзадачу, эпика с ID = " + subtask.getIdEpic() + " не существует";
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    case "DELETE": {
                        if (manager.getSubtaskHashMap().containsKey(id)) {
                            manager.deleteSubTaskForId(id);
                            response = "Подзадача c ID " + id + " успешно удалена";
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {
                            response = "Нет подзадачи с ID = " + id;
                            httpExchange.sendResponseHeaders(404, 0);
                        }
                        try (OutputStream os = httpExchange.getResponseBody()) {
                            os.write(response.getBytes());
                        }
                        break;
                    }
                    default: {
                        System.out.println(requestMethod + " - Не верный метод.");
                        httpExchange.sendResponseHeaders(405, 0);
                    }
                }
            } catch (
                    Exception e) {
                e.printStackTrace();
            }
            httpExchange.close();
        }
    }

    //  /tasks
    public class tasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            try {
                String requestMethod = httpExchange.getRequestMethod();
                if ("GET".equals(requestMethod)) {
                    String response = gson.toJson(manager.getTasks()) + "\n" +
                            gson.toJson(manager.getEpics()) + "\n" +
                            gson.toJson(manager.getSubTasks());
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
                } else {
                    System.out.println(requestMethod + " - Не верный метод.");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpExchange.close();
        }
    }

    //Адаптер для Duration в минутах
    public static class DurationAdapter extends TypeAdapter<Duration> {
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
    public static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
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

    public void srart() {
        System.out.println("Start server on port " + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Stop server on port " + PORT);
    }
}