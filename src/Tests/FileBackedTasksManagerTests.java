package Tests;
import Managers.Managers;
import Managers.FileBackedTasksManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTests extends TaskManagerTest {


    @BeforeEach
    @Override
    void setTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    void testSaveAndLoad(){
        addTasksEpicsSubtasks();
        FileBackedTasksManager fBTMFromTaskManager = (FileBackedTasksManager)taskManager;

        FileBackedTasksManager fBTManagerAfterLoad = new FileBackedTasksManager(fBTMFromTaskManager.getPath());

        fBTManagerAfterLoad = fBTManagerAfterLoad.loadFromFile(fBTMFromTaskManager.getPath());

        assertEquals(fBTMFromTaskManager, fBTManagerAfterLoad, "Не совпадают");
    }

    @Test
    void testSaveAndLoadClearTasks(){
        addTasksEpicsSubtasks();
        taskManager.clearTask();
        FileBackedTasksManager fBTMFromTaskManager = (FileBackedTasksManager)taskManager;

        FileBackedTasksManager fBTManagerAfterLoad = new FileBackedTasksManager(fBTMFromTaskManager.getPath());

        fBTManagerAfterLoad = fBTManagerAfterLoad.loadFromFile(fBTMFromTaskManager.getPath());

        assertEquals(fBTMFromTaskManager, fBTManagerAfterLoad, "Не совпадают");
    }

    @Test
    void testSaveAndLoadClearSubTasks(){
        addTasksEpicsSubtasks();
        taskManager.clearSubtask();
        FileBackedTasksManager fBTMFromTaskManager = (FileBackedTasksManager)taskManager;

        FileBackedTasksManager fBTManagerAfterLoad = new FileBackedTasksManager(fBTMFromTaskManager.getPath());

        fBTManagerAfterLoad = fBTManagerAfterLoad.loadFromFile(fBTMFromTaskManager.getPath());

        assertEquals(fBTMFromTaskManager, fBTManagerAfterLoad, "Не совпадают");
    }

    @Test
    void testSaveAndLoadClearHistory(){
        addTasksEpicsSubtasks();

        FileBackedTasksManager fBTMFromTaskManager = (FileBackedTasksManager)taskManager;

        fBTMFromTaskManager.clearHistory();

        FileBackedTasksManager fBTManagerAfterLoad = new FileBackedTasksManager(fBTMFromTaskManager.getPath());

        fBTManagerAfterLoad = fBTManagerAfterLoad.loadFromFile(fBTMFromTaskManager.getPath());

        assertFalse(fBTManagerAfterLoad.equals(fBTMFromTaskManager), "Cовпадают");
    }


    @Test
    @Override
    void testAddNewTask() {
        super.testAddNewTask();

    }

    @Test
    @Override
    void testAddNewEpic() {
        super.testAddNewEpic();
        System.out.println("");
    }

    @Test
    @Override
    void testAddNewSubTask() {
        super.testAddNewSubTask();
    }

    @Test
    @Override
    void testSearchEpicForId() {
        super.testSearchEpicForId();
    }

    @Test
    @Override
    void testSearchEpicForIdClearHash() {
        super.testSearchEpicForIdClearHash();
    }

    @Test
    @Override
    void testSearchEpicForIdIncorrect() {
        super.testSearchEpicForIdIncorrect();
    }

    @Test
    @Override
    void testSearchTaskForId() {
        super.testSearchTaskForId();
    }

    @Test
    @Override
    void testSearchTaskForIdClearHash() {
        super.testSearchTaskForIdClearHash();
    }

    @Test
    @Override
    void testSearchTaskForIdIncorrect() {
        super.testSearchTaskForIdIncorrect();
    }

    @Test
    @Override
    void testSearchSubTaskForId() {
        super.testSearchSubTaskForId();
    }

    @Test
    @Override
    void testSearchSubTaskForIdClearHash() {
        super.testSearchSubTaskForIdClearHash();
    }

    @Test
    @Override
    void testSearchSubTaskForIdIncorrect() {
        super.testSearchSubTaskForIdIncorrect();
    }

    @Test
    @Override
    void testClearEpic() {
        super.testClearEpic();
    }

    @Test
    @Override
    void testClearTask() {
        super.testClearTask();
    }

    @Test
    @Override
    void testClearSubTask() {
        super.testClearSubTask();
    }

    @Test
    @Override
    void testRefreshTask() {
        super.testRefreshTask();
    }

    @Test
    @Override
    void testRefreshTaskNull() {
        super.testRefreshTaskNull();
    }

    @Test
    @Override
    void testRefreshSubTask() {
        super.testRefreshSubTask();
    }

    @Test
    @Override
    void testRefreshSubTaskNull() {
        super.testRefreshSubTaskNull();
    }

    @Test
    @Override
    void testRefreshEpic() {
        super.testRefreshEpic();
    }

    @Test
    @Override
    void testRefreshEpicNull() {
        super.testRefreshEpicNull();
    }

    @Test
    @Override
    void testDeleteTaskForId() {
        super.testDeleteTaskForId();
    }

    @Test
    @Override
    void testDeleteTaskForIdWithIncorrectId() {
        super.testDeleteTaskForIdWithIncorrectId();
    }

    @Test
    @Override
    void testDeleteSubTaskForId() {
        super.testDeleteSubTaskForId();
    }

    @Test
    @Override
    void testDeleteSubTaskForIdWithIncorrectId() {
        super.testDeleteSubTaskForIdWithIncorrectId();
    }

    @Test
    @Override
    void testDeleteEpicForId() {
        super.testDeleteEpicForId();
    }

    @Test
    @Override
    void testDeleteEpicForIdWithIncorrectId() {
        super.testDeleteEpicForIdWithIncorrectId();
    }

}
