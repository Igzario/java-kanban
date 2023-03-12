package Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    Map<Integer, Task> getTaskHashMap();

    Map<Integer, Subtask> getSubtaskHashMap();

    Map<Integer, Epic> getEpicHashMap();

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



}
