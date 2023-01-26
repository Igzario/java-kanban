package Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    private final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    private int idTask = 1;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    @Override
    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }

    @Override
    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    @Override
    public void newTask(String name, String description) {
        Task task = new Task(idTask, name, description);
        taskHashMap.put(idTask, task);
        idTask++;

    }

    @Override
    public void newEpic(String name, String description) {
        Epic epic = new Epic(idTask, name, description);
        epicHashMap.put(idTask, epic);
        idTask++;
    }

    @Override
    public void newSubTask(String name, String description, int idEpicSearch) {
        if (!epicHashMap.containsKey(idEpicSearch)) {
            System.out.println("Нет Эпика с таким ID");
        } else {
            Subtask subtask = new Subtask(idTask, name, description, idEpicSearch);
            subtaskHashMap.put(idTask, subtask);
            epicHashMap.get(idEpicSearch).getEpicSubTasksList().add(subtask);
            idTask++;
        }
    }

    @Override
    public Epic searchEpicForId(int idEpicSearch) {
        historyManager.add(epicHashMap.get(idEpicSearch));
        return epicHashMap.get(idEpicSearch);

    }

    @Override
    public Task searchTaskForId(int idTaskSearch) {
        historyManager.add(taskHashMap.get(idTaskSearch));
        return taskHashMap.get(idTaskSearch);
    }

    @Override
    public Task searchSubtaskForId(int idSubtaskSearch) {
        historyManager.add(subtaskHashMap.get(idSubtaskSearch));
        return subtaskHashMap.get(idSubtaskSearch);
    }

    @Override
    public void clearEpic() {
        final HashMap<Integer, Epic> hashMapEpicCopy= new HashMap<>(epicHashMap);
        for (Epic epic: hashMapEpicCopy.values())
        {
            deleteEpicForId(epic.getId());
        }
    }

    @Override
    public void clearTask() {
        final HashMap<Integer, Task> hashMapTaskCopy= new HashMap<>(taskHashMap);
        for (Task task: hashMapTaskCopy.values())
        {
            deleteTaskForId(task.getId());
        }
    }

    @Override
    public void clearSubtask() {
        final HashMap<Integer, Subtask> subtaskHashMapToClear = new HashMap<>(subtaskHashMap);
        for (Subtask subtask : subtaskHashMapToClear.values()) {
            deleteSubTaskForId(subtask.getId());
        }
    }

    @Override
    public void refreshTask(Task newTask) {
        deleteTaskForId(newTask.getId());
        taskHashMap.put(newTask.getId(), newTask);
    }

    @Override
    public void refreshSubTask(Subtask newSubtask) {
        epicHashMap.get(newSubtask.getIdEpic()).getEpicSubTasksList().remove(subtaskHashMap.get(newSubtask.getId()));
        deleteSubTaskForId(newSubtask.getId());
        epicHashMap.get(newSubtask.getIdEpic()).getEpicSubTasksList().add(newSubtask);
        subtaskHashMap.put(newSubtask.getId(), newSubtask);
        epicHashMap.get(newSubtask.getIdEpic()).refreshStatus();
    }

    @Override
    public void refreshEpic(Epic newEpic) {
        newEpic.setEpicSubTasksList(epicHashMap.get(newEpic.getId()).getEpicSubTasksList());
        deleteEpicForId(newEpic.getId());
        epicHashMap.put(newEpic.getId(), newEpic);
        newEpic.refreshStatus();
    }

    @Override
    public void deleteTaskForId(int idDelete) {
        if (!taskHashMap.containsKey(idDelete)) {
            System.out.println("Нет задачи с таким ID");
        } else {
            taskHashMap.remove(idDelete);
            historyManager.remove(idDelete);
        }
    }

    @Override
    public void deleteSubTaskForId(int idDelete) {
        if (!subtaskHashMap.containsKey(idDelete)) {
            System.out.println("Нет подзадачи с таким ID");
        } else {
            epicHashMap.get(subtaskHashMap.get(idDelete).getIdEpic()).getEpicSubTasksList().remove(subtaskHashMap.get(idDelete));
            epicHashMap.get(subtaskHashMap.get(idDelete).getIdEpic()).refreshStatus();
            subtaskHashMap.remove(idDelete);
            historyManager.remove(idDelete);
        }
    }

    @Override
    public void deleteEpicForId(int idDelete) {
        if (!epicHashMap.containsKey(idDelete)) {
            System.out.println("Нет Эпика с таким ID");
        } else {
            HashMap<Integer, Subtask> hashMapSubTaskCopy = new HashMap<>(subtaskHashMap);
            for (Subtask subtask : hashMapSubTaskCopy.values()) {
                deleteSubTaskForId(subtask.getId());
            }
        }
        epicHashMap.remove(idDelete);
        historyManager.remove(idDelete);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Subtask> getSubTasksForEpicId(int idEpic) {
        return epicHashMap.get(idEpic).getEpicSubTasksList();
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n\nСписок задач:");

        for (Task task : taskHashMap.values()) {
            result.append("\nID - ");
            result.append(task.getId());
            result.append(", Название - ");
            result.append(task.getName());
            result.append(", Статус - ");
            result.append(task.getStatus());
        }
        result.append("\n\nСписок Эпиков и подзадач: ");
        for (Epic epic : epicHashMap.values()) {
            result.append("\n=========================================");
            result.append("\nID - ");
            result.append(epic.getId());
            result.append(", Название - ");
            result.append(epic.getName());
            result.append(", Статус - ");
            result.append(epic.getStatus());
            result.append("\nПодзадачи:");
            for (Subtask subtask : epic.getEpicSubTasksList()) {
                result.append("\nID - ");
                result.append(subtask.getId());
                result.append(", Название - ");
                result.append(subtask.getName());
                result.append(", Статус - ");
                result.append(subtask.getStatus());
            }
        }
        return result.toString();
    }


}