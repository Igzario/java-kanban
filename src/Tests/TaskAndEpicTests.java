package Tests;

import Managers.FileBackedTasksManager;
import Managers.Managers;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static Tasks.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskAndEpicTests {

    FileBackedTasksManager taskManager = Managers.getDefault();
    Epic savedEpicId1;
    Task savedSubTaskId2;
    Task savedSubTaskId3;
    Task savedSubTaskId4;

    void addEpicAndSubtasks() {
        Epic epic = new Epic("Test addNewEpic", "Epic addNewTask description", NEW);
        final int epicId = taskManager.newEpic(epic);
        savedEpicId1 = taskManager.searchEpicForId(epicId);

        Subtask subTask = new Subtask("Test addNewSubTask", "Test addNewSubTask description", NEW,
                LocalDateTime.of(2003, 5, 20, 6, 25), Duration.ofHours(5), epicId);
        final int subTaskId = taskManager.newSubTask(subTask);
        savedSubTaskId2 = taskManager.searchSubtaskForId(subTaskId);

        Subtask subTask2 = new Subtask("Test addNewSubTask2", "Test addNewSubTask description2", NEW,
                LocalDateTime.of(2004, 5, 20, 6, 25), Duration.ofHours(5), epicId);
        final int subTaskId2 = taskManager.newSubTask(subTask2);
        savedSubTaskId3 = taskManager.searchSubtaskForId(subTaskId2);

        Subtask subTask3 = new Subtask("Test addNewSubTask2", "Test addNewSubTask description2", NEW,
                LocalDateTime.of(2002, 5, 20, 6, 25), Duration.ofHours(5), epicId);
        final int subTaskId3 = taskManager.newSubTask(subTask3);
        savedSubTaskId4 = taskManager.searchSubtaskForId(subTaskId3);
    }

    void refreshSubtasks(Status newStatus) {
        taskManager.refreshSubTask(new Subtask(savedSubTaskId2.getId(), "New SubTask2", "Test addNewSubTask2", newStatus,
                LocalDateTime.of(2009, 5, 20, 6, 25), Duration.ofHours(5), savedEpicId1.getId()));
        taskManager.refreshSubTask(new Subtask(savedSubTaskId3.getId(), "New SubTask3", "Test addNewSubTask3", newStatus,
                LocalDateTime.of(2008, 5, 20, 6, 25), Duration.ofHours(5), savedEpicId1.getId()));
        taskManager.refreshSubTask(new Subtask(savedSubTaskId4.getId(), "New SubTask4", "Test addNewSubTask4", newStatus,
                LocalDateTime.of(2002, 5, 20, 6, 25), Duration.ofHours(5), savedEpicId1.getId()));

        savedSubTaskId2 = taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        savedSubTaskId3 = taskManager.searchSubtaskForId(savedSubTaskId3.getId());
        savedSubTaskId4 = taskManager.searchSubtaskForId(savedSubTaskId4.getId());
    }

    @Test
    void epicSubTasksClear() {
        Epic epic = new Epic("Test addNewEpic", "Epic addNewTask description", NEW);

        taskManager.newEpic(epic);
        savedEpicId1 = taskManager.searchEpicForId(1);
        assertEquals(NEW, savedEpicId1.getStatus());
    }

    @Test
    void epicSubTasksNew() {
        addEpicAndSubtasks();
        assertEquals(NEW, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(NEW, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(NEW, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(NEW, savedEpicId1.getStatus(), "Не верный статус Epic");
    }

    @Test
    void epicSubTasksDone() {
        addEpicAndSubtasks();
        Status newStatus = DONE;
        refreshSubtasks(newStatus);

        savedEpicId1 = taskManager.searchEpicForId(savedEpicId1.getId());

        assertEquals(newStatus, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(newStatus, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(newStatus, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(newStatus, savedEpicId1.getStatus(), "Не верный статус Epic");
    }

    @Test
    void epicSubTasksDoneAndNew() {
        addEpicAndSubtasks();
        Status newStatus = DONE;

        taskManager.refreshSubTask(new Subtask(savedSubTaskId2.getId(), "New SubTask2", "Test addNewSubTask2", newStatus,
                LocalDateTime.of(2002, 5, 20, 6, 25), Duration.ofHours(5), savedEpicId1.getId()));
        savedSubTaskId2 = taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        savedEpicId1 = taskManager.searchEpicForId(savedEpicId1.getId());

        assertEquals(newStatus, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(NEW, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(NEW, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(IN_PROGRESS, savedEpicId1.getStatus(), "Не верный статус Epic");
    }

    @Test
    void epicSubTasksInProgress() {
        addEpicAndSubtasks();
        Status newStatus = IN_PROGRESS;

        taskManager.refreshSubTask(new Subtask(savedSubTaskId2.getId(), "New SubTask2", "Test addNewSubTask2", newStatus,
                LocalDateTime.of(2008, 5, 20, 6, 25), Duration.ofHours(5), savedEpicId1.getId()));
        savedSubTaskId2 = taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        taskManager.refreshSubTask(new Subtask(savedSubTaskId3.getId(), "New SubTask3", "Test addNewSubTask3", newStatus,
                LocalDateTime.of(2002, 5, 20, 6, 25), Duration.ofHours(5), savedEpicId1.getId()));
        savedSubTaskId3 = taskManager.searchSubtaskForId(savedSubTaskId3.getId());
        savedEpicId1 = taskManager.searchEpicForId(savedEpicId1.getId());

        assertEquals(newStatus, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(newStatus, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(NEW, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(newStatus, savedEpicId1.getStatus(), "Не верный статус Epic");
    }
}