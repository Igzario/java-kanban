import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {


    void newTask(String name, String opisanie);

    void newEpic(String name, String opisanie);

    void newSubTask(String name, String opisanie, int idEpicSearch);

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

    HistoryManager getHistory();

    ArrayList<Subtask> getSubTasksForEpicId(int idEpic);

}
