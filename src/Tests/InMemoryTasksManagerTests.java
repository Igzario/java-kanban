package Tests;

import Managers.InMemoryTaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static Tasks.Status.NEW;

public class InMemoryTasksManagerTests extends TaskManagerTest {

    @BeforeEach
    @Override
    public void setTaskManager() {
        taskManager = new InMemoryTaskManager();
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
    }

    @Test
    @Override
    void testAddNewSubTask() {
        super.testAddNewSubTask();
    }

    @Test
    @Override
    void addTasksEpicsSubtasks() {
        super.addTasksEpicsSubtasks();
    }

    @Override
    void refreshSubtasks(Status newStatus) {
        super.refreshSubtasks(newStatus);
    }

    @Test
    @Override
    void testEpicSubTasksClear() {
        super.testEpicSubTasksClear();
    }

    @Test
    @Override
    void testEpicSubTasksNew() {
        super.testEpicSubTasksNew();
    }

    @Test
    @Override
    void testEpicSubTasksDone() {
        super.testEpicSubTasksDone();
    }

    @Test
    @Override
    void testEpicSubTasksDoneAndNew() {
        super.testEpicSubTasksDoneAndNew();
    }

    @Test
    @Override
    void testEpicSubTasksInProgress() {
        super.testEpicSubTasksInProgress();
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
}
