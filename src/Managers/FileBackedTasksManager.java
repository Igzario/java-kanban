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

    public void save() throws IOException, ManagerSaveException {
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
            throw new ManagerSaveException("Ошибка сохранения");
        }
    }


    public FileBackedTasksManager loadFromFile(Path file) throws IOException, ManagerSaveException {
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
                        continue;
                    } else if (task.getClass() == Epic.class) {
                        Epic epic = (Epic) task;
                        fileBackedTasksManager.epicHashMap.put(epic.getId(), epic);
                        continue;
                    } else if (task.getClass() == Subtask.class) {
                        Subtask subtask = (Subtask) task;
                        fileBackedTasksManager.subtaskHashMap.put(subtask.getId(), subtask);
                        fileBackedTasksManager.epicHashMap.get(subtask.getIdEpic()).getEpicSubTasksList().add(subtask);
                        continue;
                    }
                }
//                    String[] stroka = strMassiv[i].split(",");
//                            if (stroka[1].equals("task"))
//                            {
//                                Task task = new Task(Integer.parseInt(stroka[0]), stroka[2], stroka[4]);
//                                    if(stroka[3].equals("NEW")){
//                                        task.setStatus(Status.NEW);
//                                    }
//                                if(stroka[3].equals("IN_PROGRESS")){
//                                    task.setStatus(Status.IN_PROGRESS);
//                                }
//                                if(stroka[3].equals("DONE")){
//                                    task.setStatus(Status.DONE);
//                                }
//                                fileBackedTasksManager.taskHashMap.put(task.getId(), task);
//                                continue;
//                            }
//                            else if (stroka[1].equals("epic")) {
//                                Epic epic = new Epic(Integer.parseInt(stroka[0]), stroka[2], stroka[4]);
//                                if (stroka[3].equals("NEW")) {
//                                    epic.setStatus(Status.NEW);
//                                }
//                                if (stroka[3].equals("IN_PROGRESS")) {
//                                    epic.setStatus(Status.IN_PROGRESS);
//                                }
//                                if (stroka[3].equals("DONE")) {
//                                    epic.setStatus(Status.DONE);
//                                }
//                                fileBackedTasksManager.epicHashMap.put(epic.getId(), epic);
//                                continue;
//                            }
//                            else if (stroka[1].equals("subtask"))
//                            {
//                                Subtask subtask = new Subtask(Integer.parseInt(stroka[0]), stroka[2], stroka[4], Integer.parseInt(stroka[5]));
//                                if(stroka[3].equals("NEW")){
//                                    subtask.setStatus(Status.NEW);
//                                }
//                                if(stroka[3].equals("IN_PROGRESS")){
//                                    subtask.setStatus(Status.IN_PROGRESS);
//                                }
//                                if(stroka[3].equals("DONE")){
//                                    subtask.setStatus(Status.DONE);
//                                }
//                                fileBackedTasksManager.subtaskHashMap.put(subtask.getId(), subtask);
//                                continue;
//                            }
//                }}
                    else {
                        i++;
                        String[] strHistoryMassiv = strMassiv[i].split(",");
                        for (int y = 0; y < strHistoryMassiv.length; y++) {
                            if (fileBackedTasksManager.taskHashMap.containsKey(Integer.parseInt(strHistoryMassiv[y]))) {
                                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.taskHashMap.get(Integer.parseInt(strHistoryMassiv[y])));
                            } else if (fileBackedTasksManager.epicHashMap.containsKey(Integer.parseInt(strHistoryMassiv[y]))) {
                                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicHashMap.get(Integer.parseInt(strHistoryMassiv[y])));
                            } else if (fileBackedTasksManager.subtaskHashMap.containsKey(Integer.parseInt(strHistoryMassiv[y]))) {
                                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtaskHashMap.get(Integer.parseInt(strHistoryMassiv[y])));
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
        if (split[1].equals("subtask")) {
            if (split[3].equals("NEW")) {
                task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.NEW, Integer.parseInt(split[5]));
            }
            if (split[3].equals("IN_PROGRESS")) {
                task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS, Integer.parseInt(split[5]));
            }
            if (split[3].equals("DONE")) {
                task = new Subtask(Integer.parseInt(split[0]), split[2], split[4], Status.DONE, Integer.parseInt(split[5]));
            }
        }
        else if (split[1].equals("task")) {
            if (split[3].equals("NEW")) {
                task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.NEW);
            }
            if (split[3].equals("IN_PROGRESS")) {
                task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS);
            }
            if (split[3].equals("DONE")) {
                task = new Task(Integer.parseInt(split[0]), split[2], split[4], Status.DONE);
            }
        } else if (split[1].equals("epic")) {
            if (split[3].equals("NEW")) {
                task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.NEW);
            }
            if (split[3].equals("IN_PROGRESS")) {
                task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.IN_PROGRESS);
            }
            if (split[3].equals("DONE")) {
                task = new Epic(Integer.parseInt(split[0]), split[2], split[4], Status.DONE);
            }
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
    public void newTask(Task task) throws IOException, ManagerSaveException {
        super.newTask(task);
        save();
    }

    @Override
    public void newEpic(Epic epic) throws IOException, ManagerSaveException {
        super.newEpic(epic);
        save();
    }

    @Override
    public void newSubTask(Subtask subtask) throws IOException, ManagerSaveException {
        super.newSubTask(subtask);
        save();
    }

    @Override
    public Epic searchEpicForId(int idEpicSearch) throws IOException, ManagerSaveException {
        try {
            return super.searchEpicForId(idEpicSearch);
        } finally {
            save();
        }

    }

    @Override
    public Task searchTaskForId(int idTaskSearch) throws IOException, ManagerSaveException {
        try {
            return super.searchTaskForId(idTaskSearch);
        } finally {
            save();
        }
    }

    @Override
    public Task searchSubtaskForId(int idSubtaskSearch) throws IOException, ManagerSaveException {
        try {
            return super.searchSubtaskForId(idSubtaskSearch);
        } finally {
            save();
        }
    }

    @Override
    public void clearEpic() throws IOException, ManagerSaveException {
        super.clearEpic();
        save();
    }

    @Override
    public void clearTask() throws IOException, ManagerSaveException {
        super.clearTask();
        save();
    }

    @Override
    public void clearSubtask() throws IOException, ManagerSaveException {
        super.clearSubtask();
        save();
    }

    @Override
    public void refreshTask(Task newTask) throws IOException, ManagerSaveException {
        super.refreshTask(newTask);
        save();
    }

    @Override
    public void refreshSubTask(Subtask newSubtask) throws IOException, ManagerSaveException {
        super.refreshSubTask(newSubtask);
        save();
    }

    @Override
    public void refreshEpic(Epic newEpic) throws IOException, ManagerSaveException {
        super.refreshEpic(newEpic);
        save();
    }

    @Override
    public void deleteTaskForId(int idDelete) throws IOException, ManagerSaveException {
        super.deleteTaskForId(idDelete);
        save();
    }

    @Override
    public void deleteSubTaskForId(int idDelete) throws IOException, ManagerSaveException {
        super.deleteSubTaskForId(idDelete);
        save();
    }

    @Override
    public void deleteEpicForId(int idDelete) throws IOException, ManagerSaveException {
        super.deleteEpicForId(idDelete);
        save();
    }

}
