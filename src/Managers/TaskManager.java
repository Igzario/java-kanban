package Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {


    HashMap<Integer, Epic> getEpicHashMap();

    HashMap<Integer, Subtask> getSubtaskHashMap();

    HashMap<Integer, Task> getTaskHashMap();

    void newTask(String name, String description);

    void newEpic(String name, String description);

    void newSubTask(String name, String description, int idEpicSearch);

    Epic searchEpicForId(int idEpicSearch);

    Task searchTaskForId(int idTaskSearch);

    Task searchSubtaskForId(int idSubtaskSearch);

    void clearEpic();

    void clearTask();

    void clearSubtask();

    void refreshTask(Task newTask);

    void refreshSubTask(Subtask newSubtask);

    void refreshEpic(Epic newEpic);

    void deleteTaskForId(int idDelete);

    void deleteSubTaskForId(int idDelete);

    void deleteEpicForId(int idDelete);

    List<Task> getHistory();

    ArrayList<Subtask> getSubTasksForEpicId(int idEpic);

}
