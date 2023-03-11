package Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    protected final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int idTasks =1;

    public int getIdTasks() {
        return idTasks;
    }

    public void setIdTasks(int idTasks) {
        this.idTasks = idTasks;
    }



    @Override
    public List<Epic> getEpics() {
        return new ArrayList<Epic>(epicHashMap.values());
    }

    @Override
    public List<Subtask> getSubTasks() {
        return new ArrayList<Subtask>(subtaskHashMap.values());
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<Task>(taskHashMap.values());
    }

    @Override
    public int newTask(Task task) {
        int id = idTasks;
        idTasks++;
        task.setId(id);
        taskHashMap.put(id, task);
        return id;
    }

    @Override
    public int newEpic(Epic epic) {
        int id = idTasks;
        idTasks++;
        epic.setId(id);
        epicHashMap.put(id, epic);
        return id;
    }

    @Override
    public int newSubTask(Subtask subtask) {
        int id = idTasks;
        idTasks++;
        subtask.setId(id);
        subtaskHashMap.put(id, subtask);
        epicHashMap.get(subtask.getIdEpic()).getEpicSubTasksList().add(subtask);
        return id;
    }

    @Override
    public Epic searchEpicForId(int idEpicSearch) {
        Epic epic = epicHashMap.get(idEpicSearch);
        if (epic != null) {
            historyManager.add(epicHashMap.get(idEpicSearch));
        }
        return epic;
    }

    @Override
    public Task searchTaskForId(int idTaskSearch) {
        Task task = taskHashMap.get(idTaskSearch);
        if (task != null) {
            historyManager.add(taskHashMap.get(idTaskSearch));
        }
        return task;
    }

    @Override
    public Task searchSubtaskForId(int idSubtaskSearch) {
        Subtask subtask = subtaskHashMap.get(idSubtaskSearch);
        if (subtask != null) {
            historyManager.add(subtaskHashMap.get(idSubtaskSearch));
        }
        return subtask;
    }

    @Override
    public void clearEpic() {
        final HashMap<Integer, Epic> hashMapEpicCopy = new HashMap<>(epicHashMap);
        for (Epic epic : hashMapEpicCopy.values()) {
            deleteEpicForId(epic.getId());
        }
    }

    @Override
    public void clearTask() {
        final HashMap<Integer, Task> hashMapTaskCopy = new HashMap<>(taskHashMap);
        for (Task task : hashMapTaskCopy.values()) {
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
        if (newTask!=null){
            deleteTaskForId(newTask.getId());
            taskHashMap.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void refreshSubTask(Subtask newSubtask) {
        if (newSubtask!= null) {
            epicHashMap.get(newSubtask.getIdEpic()).getEpicSubTasksList().remove(subtaskHashMap.get(newSubtask.getId()));
            deleteSubTaskForId(newSubtask.getId());
            epicHashMap.get(newSubtask.getIdEpic()).getEpicSubTasksList().add(newSubtask);
            subtaskHashMap.put(newSubtask.getId(), newSubtask);
            epicHashMap.get(newSubtask.getIdEpic()).refreshStatus();
        }
    }

    @Override
    public void refreshEpic(Epic newEpic) {
        if (newEpic != null) {
            newEpic.setEpicSubTasksList(epicHashMap.get(newEpic.getId()).getEpicSubTasksList());
            deleteEpicForId(newEpic.getId());
            epicHashMap.put(newEpic.getId(), newEpic);
            newEpic.refreshStatus();
        }
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
                if (subtask.getIdEpic() == idDelete) {
                    deleteSubTaskForId(subtask.getId());
                }
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
        StringBuilder result = new StringBuilder("\nСписок задач:");

        for (Task task : taskHashMap.values()) {
            result.append("\nID - ")
                    .append(task.getId())
                    .append(", Название - ")
                    .append(task.getName())
                    .append(", Статус - ")
                    .append(task.getStatus());
        }
        result.append("\n\nСписок Эпиков и подзадач: ");
        for (Epic epic : epicHashMap.values()) {
            result.append("\n=========================================")
                    .append("\nID - ")
                    .append(epic.getId())
                    .append(", Название - ")
                    .append(epic.getName())
                    .append(", Статус - ")
                    .append(epic.getStatus())
                    .append("\nПодзадачи:");
            for (Subtask subtask : epic.getEpicSubTasksList()) {
                result.append("\nID - ")
                        .append(subtask.getId())
                        .append(", Название - ")
                        .append(subtask.getName())
                        .append(", Статус - ")
                        .append(subtask.getStatus());
            }
        }
        return result.toString();
    }
}