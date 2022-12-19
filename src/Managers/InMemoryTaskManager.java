import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Epic> epicHashMap= new HashMap<>();
    HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    HashMap<Integer, Task> taskHashMap = new HashMap<>();
    protected int idTask = 1;
    protected int idSubTask = 1;
    protected int idEpic = 1;
    protected HistoryManager historyManager = new InMemoryHistoryManager();

    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    @Override
    public void newTask(String name, String opisanie) {
        Task task = new Task(idTask, name, opisanie);
        taskHashMap.put(idTask, task);
        idTask++;
    }

    @Override
    public void newEpic(String name, String opisanie) {
        Epic epic = new Epic(idEpic, name, opisanie);
        epicHashMap.put(idEpic, epic);
        idEpic++;
    }

    @Override
    public void newSubTask(String name, String opisanie, int idEpicSearch) {
        if (!epicHashMap.containsKey(idEpicSearch)) {
            System.out.println("Нет Эпика с таким ID");
        } else {
            Subtask subtask = new Subtask(idSubTask, name, opisanie, idEpicSearch);
            subtaskHashMap.put(idSubTask, subtask);
            epicHashMap.get(idEpicSearch).epicSubTasksList.add(subtask);
            idSubTask++;
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
        epicHashMap.clear();
    }

    @Override
    public void clearTask() {
        taskHashMap.clear();
    }

    @Override
    public void clearSubtask() {
        subtaskHashMap.clear();
        for (Epic epic : epicHashMap.values()) {
            epic.epicSubTasksList.clear();
        }
    }

    @Override
    public void refreshTask(Task newTask) {
        taskHashMap.put(newTask.id, newTask);
    }

    @Override
    public void refreshSubTask(Subtask newSubtask) {
        epicHashMap.get(newSubtask.idEpic).epicSubTasksList.remove(subtaskHashMap.get(newSubtask.id));
        epicHashMap.get(newSubtask.idEpic).epicSubTasksList.add(newSubtask);
        subtaskHashMap.put(newSubtask.id, newSubtask);
        epicHashMap.get(newSubtask.idEpic).refreshStatus();
    }

    @Override
    public void refreshEpic(Epic newEpic) {
        newEpic.epicSubTasksList = searchEpicForId(newEpic.id).epicSubTasksList;
        epicHashMap.put(newEpic.id, newEpic);
        newEpic.refreshStatus();
    }

    @Override
    public void deleteTaskForId(int idDelete) {
        if (!taskHashMap.containsKey(idDelete)) {
            System.out.println("Нет задачи с таким ID");
        } else {
            taskHashMap.remove(idDelete);
        }
    }

    @Override
    public void deleteSubTaskForId(int idDelete) {
        if (!subtaskHashMap.containsKey(idDelete)) {
            System.out.println("Нет подзадачи с таким ID");
        } else {
            epicHashMap.get(subtaskHashMap.get(idDelete).idEpic).epicSubTasksList.remove(subtaskHashMap.get(idDelete));
            epicHashMap.get(subtaskHashMap.get(idDelete).idEpic).refreshStatus();
            subtaskHashMap.remove(idDelete);
        }
    }

    @Override
    public void deleteEpicForId(int idDelete) {
        if (!epicHashMap.containsKey(idDelete)) {
            System.out.println("Нет Эпика с таким ID");
        } else {
            for (Subtask subtask : epicHashMap.get(idDelete).epicSubTasksList) {
                subtaskHashMap.remove(subtask);
            }
        }
        epicHashMap.remove(idDelete);
    }

    @Override
    public HistoryManager getHistory() {
        return historyManager;
    }

    @Override
    public ArrayList<Subtask> getSubTasksForEpicId(int idEpic) {
        return epicHashMap.get(idEpic).epicSubTasksList;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("\n\nСписок задач:");

        for (Task task : taskHashMap.values()) {
            result.append("\nID - " + task.id + ", Название - " + task.name + ", Статус - " + task.status);
        }
        result.append("\n\nСписок Эпиков и подзадач: ");
        for (Epic epic : epicHashMap.values()) {
            result.append("\n=========================================");
            result.append("\nID - " + epic.id + ", Название - " + epic.name + ", Статус - " + epic.status + "\nПодзадачи:");
            for (Subtask subtask : epic.epicSubTasksList) {
                result.append("\nID - " + subtask.id + ", Название - " + subtask.name + ", Статус - " + subtask.status);
            }
        }
        return result.toString();
    }


}