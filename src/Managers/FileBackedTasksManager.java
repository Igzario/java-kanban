package Managers;
import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;

public class FileBackedTasksManager extends InMemoryTaskManager {

    protected final Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public void save() throws IOException {
        try {
            Writer fileWriter = new FileWriter(path.toString());

            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : taskHashMap.values()){
                fileWriter.write(toStringChange(task));
            }
            for (Epic epic : epicHashMap.values()){
                fileWriter.write(toStringChange(epic));
            }
            for (Subtask subtask : subtaskHashMap.values()){
                fileWriter.write(toStringChange(subtask));
            }
            fileWriter.write("\n");
            for (int i = 0; i<historyManager.getHistory().size(); i++)

            {
                if (i+1!=historyManager.getHistory().size())
                {
                    fileWriter.write(historyManager.getHistory().get(i).getId()+",");
                }
                else {
                    fileWriter.write(historyManager.getHistory().get(i).getId()+"");
                }
            }



            fileWriter.close();
        } catch (IOException e) {
            System.out.println("oops");
        }
    }


    public String toStringChange(Task task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId())
                .append(",")
                .append(task.getName())
                .append(",")
                .append(task.getStatus())
                .append(",")
                .append(task.getDescription())
                .append(",");
        if (task.getClass() == Subtask.class) {
            stringBuilder.append(((Subtask) task).getIdEpic())
                    .append("\n");
        }
        else
            stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
