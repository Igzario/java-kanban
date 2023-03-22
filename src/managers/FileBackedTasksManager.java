package managers;
import exeptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import lombok.Getter;
import lombok.Setter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    @Getter    @Setter
    private Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public void save() {
        try {
            Writer fileWriter = new FileWriter(path.toString());
            fileWriter.write("id,type,name,status,description,startTime,duration,epic\n");
            for (Task task : taskHashMap.values()) {
                fileWriter.write(toStringChange(task));
            }
            for (Epic epic : epicHashMap.values()) {
                fileWriter.write(toStringChange(epic));
            }
            for (Subtask subtask : subtaskHashMap.values()) {
                fileWriter.write(toStringChange(subtask));
            }
            fileWriter.write("\n");
            for (int i = 0; i < historyManager.getHistory().size(); i++) {
                if (i + 1 != historyManager.getHistory().size()) {
                    fileWriter.write(historyManager.getHistory().get(i).getId() + ",");
                } else {
                    fileWriter.write(historyManager.getHistory().get(i).getId() + "");
                }
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e.getCause());
        }
    }

    public FileBackedTasksManager loadFromFile(Path file) {
        FileBackedTasksManager fileBackedTasksManagerFromFile = Managers.getDefault();
        fileBackedTasksManagerFromFile.setPath(file);
        try {
            String str = Files.readString(file);
            str = str.replace("id,type,name,status,description,startTime,duration,epic\n", "");
            String[] strMassiv = str.split("\n");
            for (int i = 0; i < strMassiv.length; i++) {
                if (!strMassiv[i].equals("")) {
                    Task task = fromString(strMassiv[i]);
                    if (task.getClass() == Task.class) {
                        fileBackedTasksManagerFromFile.taskHashMap.put(task.getId(), task);
                        idTasks++;
                    } else if (task.getClass() == Epic.class) {
                        Epic epic = (Epic) task;
                        fileBackedTasksManagerFromFile.epicHashMap.put(epic.getId(), epic);
                        idTasks++;
                    } else if (task.getClass() == Subtask.class) {
                        Subtask subtask = (Subtask) task;
                        fileBackedTasksManagerFromFile.subtaskHashMap.put(subtask.getId(), subtask);
                        fileBackedTasksManagerFromFile.epicHashMap.get(subtask.getIdEpic()).getEpicSubTasksList().add(subtask);
                        fileBackedTasksManagerFromFile.epicHashMap.get(subtask.getIdEpic()).refreshTime();
                        idTasks++;
                    }
                } else {
                    i++;
                    String[] strHistoryMassiv = strMassiv[i].split(",");
                    for (String s : strHistoryMassiv) {
                        if (fileBackedTasksManagerFromFile.taskHashMap.containsKey(Integer.parseInt(s))) {
                            fileBackedTasksManagerFromFile.historyManager.add(fileBackedTasksManagerFromFile.taskHashMap.get(Integer.parseInt(s)));
                        } else if (fileBackedTasksManagerFromFile.epicHashMap.containsKey(Integer.parseInt(s))) {
                            fileBackedTasksManagerFromFile.historyManager.add(fileBackedTasksManagerFromFile.epicHashMap.get(Integer.parseInt(s)));
                        } else if (fileBackedTasksManagerFromFile.subtaskHashMap.containsKey(Integer.parseInt(s))) {
                            fileBackedTasksManagerFromFile.historyManager.add(fileBackedTasksManagerFromFile.subtaskHashMap.get(Integer.parseInt(s)));
                        }
                    }
                }
            }

            fileBackedTasksManagerFromFile.idTasks = this.idTasks;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения", e.getCause());
        }
        return fileBackedTasksManagerFromFile;
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        Task task = null;
        switch (split[1]) {
            case "subtask":
                switch (split[3]) {
                    case "NEW":
                        task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.NEW,
                                LocalDateTime.parse(split[5]), Duration.parse(split[6]), Integer.parseInt(split[7]));
                        break;
                    case "IN_PROGRESS":
                        task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS,
                                LocalDateTime.parse(split[5]), Duration.parse(split[6]), Integer.parseInt(split[7]));
                        break;
                    case "DONE":
                        task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.DONE,
                                LocalDateTime.parse(split[5]), Duration.parse(split[6]), Integer.parseInt(split[7]));
                }
                break;
            case "task":
                switch (split[3]) {
                    case "NEW":
                        task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.NEW
                                , LocalDateTime.parse(split[5]), Duration.parse(split[6]));
                        break;
                    case "IN_PROGRESS":
                        task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS,
                                LocalDateTime.parse(split[5]), Duration.parse(split[6]));
                        break;
                    case "DONE":
                        task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.DONE,
                                LocalDateTime.parse(split[5]), Duration.parse(split[6]));
                        break;
                }
                break;
            case "epic":
                switch (split[3]) {
                    case "NEW":
                        task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.NEW);
                        task.setDuration(Duration.parse(split[6]));
                        task.setStartTime(LocalDateTime.parse(split[5]));
                        break;
                    case "IN_PROGRESS":
                        task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS);
                        task.setDuration(Duration.parse(split[6]));
                        task.setStartTime(LocalDateTime.parse(split[5]));
                        break;
                    case "DONE":
                        task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.DONE);
                        task.setDuration(Duration.parse(split[6]));
                        task.setStartTime(LocalDateTime.parse(split[5]));
                        break;
                }
                break;
        }
        return task;
    }

    public String toStringChange(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId())
                .append(",");
        if (task.getClass() == Epic.class) {
            stringBuilder.append("epic");
        } else if (task.getClass() == Task.class) {
            stringBuilder.append("task");
        } else if (task.getClass() == Subtask.class) {
            stringBuilder.append("subtask");
        }
        stringBuilder.append(",")
                .append(task.getName())
                .append(",")
                .append(task.getStatus())
                .append(",")
                .append(task.getDescription())
                .append(",")
                .append(task.getStartTime())
                .append(",")
                .append(task.getDuration())
                .append(",")
        ;
        if (task.getClass() == Subtask.class) {
            stringBuilder.append(((Subtask) task).getIdEpic())
                    .append("\n");
        } else
            stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public void clearHistory(){
        for (Task task : getHistory()){
            historyManager.remove(task.getId());
        }
    }

    @Override
    public int newTask(Task task) {
        checkTaskTime(task);
        int id = idTasks;
        idTasks++;
        task.setId(id);
        taskHashMap.put(id, task);
        sortedTasksTreeSet.add(task);
        prioritizedTasks(task);
        save();
        return id;
    }

    @Override
    public int newEpic(Epic epic) {
        int id = idTasks;
        idTasks++;
        epic.setId(id);
        epicHashMap.put(id, epic);
        save();
        return id;
    }

    @Override
    public int newSubTask(Subtask subtask) {
        checkTaskTime(subtask);
        int id = idTasks;
        idTasks++;
        subtask.setId(id);
        subtaskHashMap.put(id, subtask);
        epicHashMap.get(subtask.getIdEpic()).getEpicSubTasksList().add(subtask);
        epicHashMap.get(subtask.getIdEpic()).refreshStatusAndTime();
        sortedTasksTreeSet.add(subtask);
        prioritizedTasks(subtask);
        save();
        return id;
    }

    @Override
    public Epic searchEpicForId(int idEpicSearch) {
        historyManager.add(epicHashMap.get(idEpicSearch));
        save();
        return epicHashMap.get(idEpicSearch);

    }

    @Override
    public Task searchTaskForId(int idTaskSearch) {
        historyManager.add(taskHashMap.get(idTaskSearch));
        save();
        return taskHashMap.get(idTaskSearch);
    }

    @Override
    public Task searchSubtaskForId(int idSubtaskSearch) {
        historyManager.add(subtaskHashMap.get(idSubtaskSearch));
        save();
        return subtaskHashMap.get(idSubtaskSearch);
    }

    @Override
    public void clearEpic() {
        super.clearEpic();
        save();
    }

    @Override
    public void clearTask() {
        super.clearTask();
        save();
    }

    @Override
    public void clearSubtask() {
        super.clearSubtask();
        save();
    }

    @Override
    public void refreshTask(Task newTask) {
        super.refreshTask(newTask);
        save();
    }

    @Override
    public void refreshSubTask(Subtask newSubtask) {
        super.refreshSubTask(newSubtask);
        save();
    }

    @Override
    public void refreshEpic(Epic newEpic) {
        super.refreshEpic(newEpic);
        save();
    }

    @Override
    public void deleteTaskForId(int idDelete) {
        super.deleteTaskForId(idDelete);
        save();
    }

    @Override
    public void deleteSubTaskForId(int idDelete) {
        super.deleteSubTaskForId(idDelete);
        save();
    }

    @Override
    public void deleteEpicForId(int idDelete) {
        super.deleteEpicForId(idDelete);
        save();
    }

    @Override
    public boolean equals(Object obj) {
        FileBackedTasksManager test;
        boolean isOk = false;
        if (this == obj) {
            return true;
        }
        if (obj.getClass() == FileBackedTasksManager.class) {
            test = (FileBackedTasksManager) obj;
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
                    isOk=true;
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