package Managers;
import Exeptions.ManagerSaveException;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    HashMap<Integer, Epic> getEpicHashMap();

    HashMap<Integer, Subtask> getSubtaskHashMap();

    HashMap<Integer, Task> getTaskHashMap();

    void newTask(Task task) throws IOException, ManagerSaveException;

    void newEpic(Epic epic) throws IOException, ManagerSaveException;

    void newSubTask(Subtask subtask) throws IOException, ManagerSaveException;

    Epic searchEpicForId(int idEpicSearch) throws IOException, ManagerSaveException;

    Task searchTaskForId(int idTaskSearch) throws IOException, ManagerSaveException;

    Task searchSubtaskForId(int idSubtaskSearch) throws IOException, ManagerSaveException;

    void clearEpic() throws IOException, ManagerSaveException;

    void clearTask() throws IOException, ManagerSaveException;

    void clearSubtask() throws IOException, ManagerSaveException;

    void refreshTask(Task newTask) throws IOException, ManagerSaveException;

    void refreshSubTask(Subtask newSubtask) throws IOException, ManagerSaveException;

    void refreshEpic(Epic newEpic) throws IOException, ManagerSaveException;

    void deleteTaskForId(int idDelete) throws IOException, ManagerSaveException;

    void deleteSubTaskForId(int idDelete) throws IOException, ManagerSaveException;

    void deleteEpicForId(int idDelete) throws IOException, ManagerSaveException;

    List<Task> getHistory();

    ArrayList<Subtask> getSubTasksForEpicId(int idEpic);
}
