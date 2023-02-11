package Managers;
import Exeptions.ManagerSaveException;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTasksManager extends InMemoryTaskManager {

    protected final Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public void save() {
        try {
            Writer fileWriter = new FileWriter(path.toString());
            fileWriter.write("id,type,name,status,description,epic\n");
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
            try {
                throw new ManagerSaveException("Ошибка сохранения", e.getCause());
            }
            catch (ManagerSaveException exception){
                System.out.println(exception.getMessage()+exception.getCause());
            }
        }
    }

    public FileBackedTasksManager loadFromFile(Path file) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            String str = Files.readString(file);
            str= str.replace("id,type,name,status,description,epic\n", "");
            String[] strMassiv = str.split("\n");
            for(int i = 0 ; i< strMassiv.length; i++) {
                if (!strMassiv[i].equals("")) {
                    Task task = fromString(strMassiv[i]);
                    if (task.getClass() == Task.class) {
                        fileBackedTasksManager.taskHashMap.put(task.getId(), task);
                    } else if (task.getClass() == Epic.class) {
                        Epic epic = (Epic) task;
                        fileBackedTasksManager.epicHashMap.put(epic.getId(), epic);
                    } else if (task.getClass() == Subtask.class) {
                        Subtask subtask = (Subtask) task;
                        fileBackedTasksManager.subtaskHashMap.put(subtask.getId(), subtask);
                        fileBackedTasksManager.epicHashMap.get(subtask.getIdEpic()).getEpicSubTasksList().add(subtask);
                    }
                }
                    else {
                        i++;
                        String[] strHistoryMassiv = strMassiv[i].split(",");
                    for (String s : strHistoryMassiv) {
                        if (fileBackedTasksManager.taskHashMap.containsKey(Integer.parseInt(s))) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.taskHashMap.get(Integer.parseInt(s)));
                        } else if (fileBackedTasksManager.epicHashMap.containsKey(Integer.parseInt(s))) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicHashMap.get(Integer.parseInt(s)));
                        } else if (fileBackedTasksManager.subtaskHashMap.containsKey(Integer.parseInt(s))) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtaskHashMap.get(Integer.parseInt(s)));
                        }
                    }
                    }
                }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения");
        }
        return fileBackedTasksManager;
    }

    public Task fromString(String value) {
        String[] split = value.split(",");
        Task task = null;
            switch (split[1]) {
                case "subtask":
                    switch (split[3]) {
                        case "NEW":
                            task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.NEW, Integer.parseInt(split[5]));
                            break;
                        case "IN_PROGRESS":
                            task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS, Integer.parseInt(split[5]));
                            break;
                        case "DONE":
                            task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.DONE, Integer.parseInt(split[5]));
                    }
                    break;
                case "task":
                    switch (split[3]) {
                        case "NEW":
                            task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.NEW);
                            break;
                        case "IN_PROGRESS":
                            task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS);
                            break;
                        case "DONE":
                            task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.DONE);
                            break;
                    }
                    break;
                case "epic":
                    switch (split[3]) {
                        case "NEW":
                            task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.NEW);
                            break;
                        case "IN_PROGRESS":
                            task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS);
                            break;
                        case "DONE":
                            task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.DONE);
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
                .append(",");
        if (task.getClass() == Subtask.class) {
            stringBuilder.append(((Subtask) task).getIdEpic())
                    .append("\n");
        } else
            stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    @Override
    public void newTask(Task task) {
        super.newTask(task);
        save();
    }

    @Override
    public void newEpic(Epic epic) {
        super.newEpic(epic);
        save();
    }

    @Override
    public void newSubTask(Subtask subtask) {
        super.newSubTask(subtask);
        save();
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
}
