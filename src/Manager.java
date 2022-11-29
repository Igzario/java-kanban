import java.util.ArrayList;
import java.util.HashMap;

public class Manager {


    private HashMap<Integer, Epic> epicHashMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskHashMap= new HashMap<>();
    private HashMap<Integer, Task> taskHashMap= new HashMap<>();
    private int idTask = 1;
    private int idSubTask = 1;
    private int idEpic = 1;



    public HashMap<Integer, Epic> getEpicHashMap() {
        return epicHashMap;
    }

    public HashMap<Integer, Subtask> getSubtaskHashMap() {
        return subtaskHashMap;
    }

    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    public Task newTask(String name, String opisanie) {
        Task task = new Task(idTask, name, opisanie);

        taskHashMap.put(idTask, task);
        idTask++;
        return task;

    }


    public Epic newEpic(String name, String opisanie) {
        Epic epic = new Epic(idEpic, name, opisanie);
        epicHashMap.put(idEpic, epic);
        idEpic++;
        return epic;
    }



    public Subtask newSubTask(String name, String opisanie, int idEpicSearch) {

        Subtask subtask = new Subtask(idSubTask, name, opisanie, idEpicSearch);
        subtaskHashMap.put(idSubTask, subtask);
        searchEpicForId(idEpicSearch).podZadachi.add(subtask);
        idSubTask++;
        return subtask;

    }

    Epic searchEpicForId(int idEpicSearch) {
        return epicHashMap.get(idEpicSearch);
    }

    Task searchTaskForId(int idTaskSearch) {
        return taskHashMap.get(idTaskSearch);
    }

    Task searchSubtaskForId(int idSubtaskSearch) {
        return subtaskHashMap.get(idSubtaskSearch);
    }


    public void clearEpic() {
        epicHashMap.clear();
    }

    public void clearTask() {
        taskHashMap.clear();
    }

    public void clearSubtask() {
        subtaskHashMap.clear();
    }

    public void refreshTask(Task newTask, int id) {
        taskHashMap.put(id, newTask);
    }

    public void refreshSubTask(Subtask newSubtask) {
        epicHashMap.get(newSubtask.idEpic).podZadachi.remove(subtaskHashMap.get(newSubtask.id));
        epicHashMap.get(newSubtask.idEpic).podZadachi.add(newSubtask);
        subtaskHashMap.put(newSubtask.id, newSubtask);
        epicHashMap.get(newSubtask.idEpic).refreshStatus();

    }

    public void refreshEpic(Epic newEpic) {
        newEpic.podZadachi = searchEpicForId(newEpic.id).podZadachi;
        epicHashMap.put(newEpic.id, newEpic);
        newEpic.refreshStatus();
    }

    public void deleteTaskForId(int idDelete) {
        taskHashMap.remove(idDelete);
    }

    public void deleteSubTaskForId(int idDelete) {
        epicHashMap.get(subtaskHashMap.get(idDelete).idEpic).podZadachi.remove(subtaskHashMap.get(idDelete));
                epicHashMap.get(subtaskHashMap.get(idDelete).idEpic).refreshStatus();
        subtaskHashMap.remove(idDelete);
    }

    public void deleteEpicForId(int idDelete) {
        for (Subtask subtask : epicHashMap.get(idDelete).podZadachi) {
            subtaskHashMap.remove(subtask);
        }
        epicHashMap.remove(idDelete);
    }

    public ArrayList getSubTasksForEpicId (int idEpic){
       return epicHashMap.get(idEpic).podZadachi;

    }

    @Override
    public String toString(){
        String result = "\n\nСписок задач:";

        for (Task task : taskHashMap.values()){
          result= result +"\nID - " + task.id + ", Название - "+ task.name+ ", Статус - "+task.status;
        }


         result = result + "\n\nСписок Эпиков и подзадач: " ;

            for(Epic epic : epicHashMap.values()){
                result=result+"\n=========================================";
                result=result + "\nID - " + epic.id + ", Название - " + epic.name +", Статус - "+epic.status+ "\nПодзадачи:";
                    for (Subtask subtask : epic.podZadachi){
                        result=result + "\nID - " + subtask.id + ", Название - " + subtask.name+", Статус - "+subtask.status;

                    }


            }

return result;
    }

}
