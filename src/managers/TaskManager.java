package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.*;

public interface TaskManager {

    List<Epic> getEpics();

    List<Subtask> getSubTasks();

    List<Task> getTasks();

    int newTask(Task task);

    int newEpic(Epic epic);

    int newSubTask(Subtask subtask);

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

    LinkedList<Task> getPrioritizedTasks();

    void checkTaskTime(Task task);
}
