package Managers;
import Exeptions.ManagerSaveException;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskHashMap = new HashMap<>();
    protected final HashMap<Integer, Task> taskHashMap = new HashMap<>();


    protected final HistoryManager historyManager = Managers.getDefaultHistory();

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
    public void newTask(Task task) throws IOException, ManagerSaveException {
        taskHashMap.put(task.getId(), task);
    }

    @Override
    public void newEpic(Epic epic) throws IOException, ManagerSaveException {
        epicHashMap.put(epic.getId(), epic);

    }

    @Override
    public void newSubTask(Subtask subtask) throws IOException, ManagerSaveException {
            subtaskHashMap.put(subtask.getId(), subtask);
            epicHashMap.get(subtask.getIdEpic()).getEpicSubTasksList().add(subtask);
        }


    @Override
    public Epic searchEpicForId(int idEpicSearch) throws IOException, ManagerSaveException {
        historyManager.add(epicHashMap.get(idEpicSearch));
        return epicHashMap.get(idEpicSearch);

    }

    @Override
    public Task searchTaskForId(int idTaskSearch) throws IOException, ManagerSaveException {
        historyManager.add(taskHashMap.get(idTaskSearch));
        return taskHashMap.get(idTaskSearch);
    }

    @Override
    public Task searchSubtaskForId(int idSubtaskSearch) throws IOException, ManagerSaveException {
        historyManager.add(subtaskHashMap.get(idSubtaskSearch));
        return subtaskHashMap.get(idSubtaskSearch);
    }

    @Override
    public void clearEpic() throws IOException, ManagerSaveException {
        final HashMap<Integer, Epic> hashMapEpicCopy = new HashMap<>(epicHashMap);
        for (Epic epic : hashMapEpicCopy.values()) {
            deleteEpicForId(epic.getId());
        }
    }

    @Override
    public void clearTask() throws IOException, ManagerSaveException {
        final HashMap<Integer, Task> hashMapTaskCopy = new HashMap<>(taskHashMap);
        for (Task task : hashMapTaskCopy.values()) {
            deleteTaskForId(task.getId());
        }
    }

    @Override
    public void clearSubtask() throws IOException, ManagerSaveException {
        final HashMap<Integer, Subtask> subtaskHashMapToClear = new HashMap<>(subtaskHashMap);
        for (Subtask subtask : subtaskHashMapToClear.values()) {
            deleteSubTaskForId(subtask.getId());
        }
    }

    @Override
    public void refreshTask(Task newTask) throws IOException, ManagerSaveException {
        deleteTaskForId(newTask.getId());
        taskHashMap.put(newTask.getId(), newTask);
    }

    @Override
    public void refreshSubTask(Subtask newSubtask) throws IOException, ManagerSaveException {
        epicHashMap.get(newSubtask.getIdEpic()).getEpicSubTasksList().remove(subtaskHashMap.get(newSubtask.getId()));
        deleteSubTaskForId(newSubtask.getId());
        epicHashMap.get(newSubtask.getIdEpic()).getEpicSubTasksList().add(newSubtask);
        subtaskHashMap.put(newSubtask.getId(), newSubtask);
        epicHashMap.get(newSubtask.getIdEpic()).refreshStatus();
    }

    @Override
    public void refreshEpic(Epic newEpic) throws IOException, ManagerSaveException {
        newEpic.setEpicSubTasksList(epicHashMap.get(newEpic.getId()).getEpicSubTasksList());
        deleteEpicForId(newEpic.getId());
        epicHashMap.put(newEpic.getId(), newEpic);
        newEpic.refreshStatus();
    }

    @Override
    public void deleteTaskForId(int idDelete) throws IOException, ManagerSaveException {
        if (!taskHashMap.containsKey(idDelete)) {
            System.out.println("Нет задачи с таким ID");
        } else {
            taskHashMap.remove(idDelete);
            historyManager.remove(idDelete);
        }
    }

    @Override
    public void deleteSubTaskForId(int idDelete) throws IOException, ManagerSaveException {
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
    public void deleteEpicForId(int idDelete) throws IOException, ManagerSaveException {
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
        StringBuilder result = new StringBuilder("\n\nСписок задач:");

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