package managers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpTaskManager extends FileBackedTasksManager {
    URL url;
    KVTaskClient kvTaskClient;
    GsonBuilder gsonBuilder;
    Gson gson;

    public HttpTaskManager(URL url) throws IOException, InterruptedException {
        this.url = url;
        this.kvTaskClient = new KVTaskClient(url);
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();
    }

    public void save() {
        try {
            kvTaskClient.put("tasks:", gson.toJson(taskHashMap));
            kvTaskClient.put("epics:", gson.toJson(epicHashMap));
            kvTaskClient.put("subtasks:", gson.toJson(subtaskHashMap));
            kvTaskClient.put("history:", gson.toJson(getHistory()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HttpTaskManager load() throws IOException, InterruptedException {
        int idTasks = 1;

        HttpTaskManager manager = Managers.getHttpTaskManager();
        String tasks = kvTaskClient.load("tasks:");
        String epics = kvTaskClient.load("epics:");
        String history = kvTaskClient.load("history:");

        HashMap<Integer, LinkedTreeMap> tasksHash = (gson.fromJson(tasks, HashMap.class));
        HashMap<Integer, LinkedTreeMap> epicsHash = (gson.fromJson(epics, HashMap.class));
        ArrayList<LinkedTreeMap> historyHash = (gson.fromJson(history, ArrayList.class));

        for (LinkedTreeMap map : tasksHash.values()) {
            int id = (int) ((double) map.get("id"));
            String name = (String) map.get("name");
            String description = (String) map.get("description");
            Status status = Status.NEW;
            String statusStr = (String) map.get("status");
            switch (statusStr) {
                case "IN_PROGRESS":
                    status = Status.IN_PROGRESS;
                case "DONE":
                    status = Status.DONE;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime startTime = LocalDateTime.parse((String) map.get("startTime"), formatter);
            Duration duration = Duration.ofMinutes((long) ((double) map.get("duration")));
            Task task = new Task(id, name, description, status, startTime, duration);
            manager.getTaskHashMap().put(task.getId(), task);
            manager.sortedTasksTreeSet.add(task);
            manager.prioritizedTasks(task);
            idTasks++;
        }
        for (LinkedTreeMap epickMap : epicsHash.values()) {
            ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) epickMap.get("epicSubTasksList");

            int id = (int) ((double) epickMap.get("id"));
            String name = (String) epickMap.get("name");
            String description = (String) epickMap.get("description");
            Status status = Status.NEW;
            String statusStr = (String) epickMap.get("status");
            switch (statusStr) {
                case "IN_PROGRESS":
                    status = Status.IN_PROGRESS;
                case "DONE":
                    status = Status.DONE;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            Epic epic = new Epic(id, name, description, status);
            for (LinkedTreeMap subTaskMap : list) {
                int idEpic = (int) ((double) subTaskMap.get("idEpic"));
                int idSubtask = (int) ((double) subTaskMap.get("id"));
                String nameSubtask = (String) subTaskMap.get("name");
                String descriptionSubtask = (String) subTaskMap.get("description");
                Status statusSubtask = Status.NEW;
                switch (statusStr) {
                    case "IN_PROGRESS":
                        statusSubtask = Status.IN_PROGRESS;
                    case "DONE":
                        statusSubtask = Status.DONE;
                }
                LocalDateTime startTimeSubtask = LocalDateTime.parse((String) subTaskMap.get("startTime"), formatter);
                Duration durationSubtask = Duration.ofMinutes((long) ((double) subTaskMap.get("duration")));
                Subtask subTask = new Subtask(idSubtask, nameSubtask, descriptionSubtask, statusSubtask, startTimeSubtask, durationSubtask, idEpic);
                manager.getSubtaskHashMap().put(subTask.getId(), subTask);
                epic.getEpicSubTasksList().add(subTask);
                epic.refreshTime();
                manager.sortedTasksTreeSet.add(subTask);
                manager.prioritizedTasks(subTask);
                idTasks++;
            }
            manager.getEpicHashMap().put(epic.getId(), epic);
            idTasks++;
        }
        manager.idTasks = idTasks;

        for (LinkedTreeMap map : historyHash) {
            int id = (int) ((double) map.get("id"));
            if (manager.getTaskHashMap().containsKey(id)) {
                manager.historyManager.add(manager.getTaskHashMap().get(id));
            } else if (manager.getEpicHashMap().containsKey(id)) {
                manager.historyManager.add(manager.getEpicHashMap().get(id));
            } else if (manager.getSubtaskHashMap().containsKey(id)) {
                manager.historyManager.add(manager.getSubtaskHashMap().get(id));
            }
        }
        return manager;
    }

    //Адаптер для Duration в минутах
    static class DurationAdapter extends TypeAdapter<Duration> {
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
    static class LocalDateAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
            jsonWriter.value(localDate.format(formatter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatter);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\nHttpTaskManager:\n");
        result.append("\nTasks:");
        for (Task task : taskHashMap.values()) {
            result.append(",")
                    .append(task.toString());
        }
        result.append("\nEpics: ");
        for (Epic epic : epicHashMap.values()) {
            result.append(",")
                    .append(epic.toString());
        }
        for (Subtask subtask : subtaskHashMap.values()) {
            result.append(",")
                    .append(subtask.toString());
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        FileBackedTasksManager test;
        boolean isOk = false;
        if (this == obj) {
            return true;
        }
        if (obj.getClass() == HttpTaskManager.class) {
            test = (HttpTaskManager) obj;
        } else {
            return false;
        }
        if (this.taskHashMap.size() == test.taskHashMap.size() && this.subtaskHashMap.size() == test.subtaskHashMap.size()
                && this.epicHashMap.size() == test.epicHashMap.size() && this.getHistory().size() == test.getHistory().size()) {
            for (Task task : this.taskHashMap.values()) {
                isOk = test.taskHashMap.containsKey(task.getId());
                if (!isOk) {
                    return false;
                }
            }
            for (Subtask subTask : this.subtaskHashMap.values()) {
                isOk = test.subtaskHashMap.containsKey(subTask.getId());
                if (!isOk) {
                    return false;
                }
            }
            for (Epic epic : this.epicHashMap.values()) {
                isOk = test.epicHashMap.containsKey(epic.getId());
                if (!isOk) {
                    return false;
                }
            }
            int i = 0;
            for (Task task : this.getHistory()) {
                Task testTask = test.getHistory().get(i);
                if (testTask == null) {
                    return false;
                }
                if (task.getId() == testTask.getId()) {
                    isOk = true;
                }
                if (!isOk) {
                    return false;
                }
                i++;
            }
        }
        return isOk;
    }
}