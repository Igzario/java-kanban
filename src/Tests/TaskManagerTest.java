package Tests;

import Managers.TaskManager;
import Tasks.Epic;
import Tasks.Status;
import Tasks.Subtask;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static Tasks.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;
    Epic savedEpicId1;
    Task savedSubTaskId2;
    Task savedSubTaskId3;
    Task savedSubTaskId4;
    Task savedTaskId5;
    Task savedTaskId6;

    abstract void setTaskManager();

    @Test
    void testLocalDateTimeForEpic() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2022, 11, 5, 13, 5);
        Duration duration1 = Duration.ofMinutes(150);
        LocalDateTime localDateTime2 = LocalDateTime.of(2023, 11, 5, 13, 5);
        Duration duration2 = Duration.ofMinutes(300);
        LocalDateTime localDateTime3 = LocalDateTime.of(2020, 11, 5, 13, 5);
        Duration duration3 = Duration.ofMinutes(500);

        Epic epic = new Epic(1, "Test addNewEpic", "Epic addNewTask description", NEW);
        final int epicId = taskManager.newEpic(epic);

        Subtask subTask = new Subtask(5, "Test addNewSubTask", "Test addNewSubTask description", NEW
                , localDateTime1, duration1, epicId);
        epic.getEpicSubTasksList().add(subTask);

        Subtask subTask2 = new Subtask(6, "Test addNewSubTask2", "Test addNewSubTask description2", NEW
                , localDateTime2, duration2, epicId);
        epic.getEpicSubTasksList().add(subTask2);

        Subtask subTask3 = new Subtask(8, "Test addNewSubTask2", "Test addNewSubTask description2", NEW,
                localDateTime3, duration3, epicId);
        epic.getEpicSubTasksList().add(subTask3);

        Duration checkDuration = duration1.plus(duration2).plus(duration3);
        epic.refreshTime();

        List<Subtask> subTasksToCheck = List.of(subTask3, subTask, subTask2);
        ArrayList<Subtask> subTasks = epic.getEpicSubTasksList();

        for (int i = 0; i < subTasks.size(); i++) {
            assertEquals(subTasks.get(i), subTasksToCheck.get(i));
        }
        assertEquals(checkDuration, epic.getDuration(), "Duration не совпадают.");
    }

    void testAddNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        final int taskId = taskManager.newTask(task);
        final Task savedTask = taskManager.searchTaskForId(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    void testAddNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Epic addNewTask description", NEW);
        final int epicId = taskManager.newEpic(epic);
        final Epic savedEpic = taskManager.searchEpicForId(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    void testAddNewSubTask() {
        Epic epic = new Epic("Test addNewEpic", "Epic addNewEpic description", NEW);
        final int epicId = taskManager.newEpic(epic);

        Subtask subTask = new Subtask("Test addNewSubTask", "Test addNewSubTask description", NEW, epicId);
        final int subTaskId = taskManager.newSubTask(subTask);

        final Task savedSubTask = taskManager.searchSubtaskForId(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<Subtask> subtasks = taskManager.getSubTasks();

        assertNotNull(subtasks, "Задачи на возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subtasks.get(0), "Задачи не совпадают.");
    }

    void addTasksEpicsSubtasks() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2022, 12, 9, 22, 5);
        Duration duration1 = Duration.ofMinutes(150);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022, 9, 4, 21, 5);
        Duration duration2 = Duration.ofMinutes(300);
        LocalDateTime localDateTime3 = LocalDateTime.of(2019, 4, 7, 18, 5);
        Duration duration3 = Duration.ofMinutes(500);
        LocalDateTime localDateTime4 = LocalDateTime.of(2023, 2, 15, 15, 5);
        Duration duration4 = Duration.ofMinutes(150);
        LocalDateTime localDateTime5 = LocalDateTime.of(2019, 5, 9, 9, 5);
        Duration duration5 = Duration.ofMinutes(300);
        LocalDateTime localDateTime6 = LocalDateTime.of(2023, 7, 25, 10, 5);
        Duration duration6 = Duration.ofMinutes(500);


        Epic epic = new Epic("Test addNewEpic", "Epic addNewTask description", NEW, localDateTime1, duration1);
        final int epicId = taskManager.newEpic(epic);
        savedEpicId1 = taskManager.searchEpicForId(epicId);

        Subtask subTask = new Subtask("Test addNewSubTask", "Test addNewSubTask description", NEW
                , localDateTime2, duration2, epicId);
        final int subTaskId = taskManager.newSubTask(subTask);
        savedSubTaskId2 = taskManager.searchSubtaskForId(subTaskId);

        Subtask subTask2 = new Subtask("Test addNewSubTask2", "Test addNewSubTask description2", NEW
                , localDateTime3, duration3, epicId);
        final int subTaskId2 = taskManager.newSubTask(subTask2);
        savedSubTaskId3 = taskManager.searchSubtaskForId(subTaskId2);

        Subtask subTask3 = new Subtask("Test addNewSubTask2", "Test addNewSubTask description2", NEW
                , localDateTime4, duration4, epicId);
        final int subTaskId3 = taskManager.newSubTask(subTask3);
        savedSubTaskId4 = taskManager.searchSubtaskForId(subTaskId3);

        Task task1 = new Task("Test addNewTask1", "Test addNewTask description2", NEW
                , localDateTime5, duration5);
        final int taskId1 = taskManager.newTask(task1);
        savedTaskId5 = taskManager.searchTaskForId(taskId1);

        Task task2 = new Task("Test addNewTask2", "Test addNewTask description2", NEW
                , localDateTime6, duration6);
        final int taskId2 = taskManager.newTask(task2);
        savedTaskId6 = taskManager.searchTaskForId(taskId2);
    }

    void refreshSubtasks(Status newStatus) {
        taskManager.refreshSubTask(new Subtask(savedSubTaskId2.getId(), "New SubTask2", "Test addNewSubTask2", newStatus, savedEpicId1.getId()));
        taskManager.refreshSubTask(new Subtask(savedSubTaskId3.getId(), "New SubTask3", "Test addNewSubTask3", newStatus, savedEpicId1.getId()));
        taskManager.refreshSubTask(new Subtask(savedSubTaskId4.getId(), "New SubTask4", "Test addNewSubTask4", newStatus, savedEpicId1.getId()));

        savedSubTaskId2 = taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        savedSubTaskId3 = taskManager.searchSubtaskForId(savedSubTaskId3.getId());
        savedSubTaskId4 = taskManager.searchSubtaskForId(savedSubTaskId4.getId());
    }

    void testEpicSubTasksClear() {
        testAddNewEpic();
        savedEpicId1 = taskManager.searchEpicForId(1);
        assertEquals(NEW, savedEpicId1.getStatus());
    }

    void testEpicSubTasksNew() {
        addTasksEpicsSubtasks();
        assertEquals(NEW, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(NEW, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(NEW, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(NEW, savedEpicId1.getStatus(), "Не верный статус Epic");
    }

    void testEpicSubTasksDone() {
        addTasksEpicsSubtasks();
        Status newStatus = DONE;
        refreshSubtasks(newStatus);

        savedEpicId1 = taskManager.searchEpicForId(savedEpicId1.getId());

        assertEquals(newStatus, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(newStatus, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(newStatus, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(newStatus, savedEpicId1.getStatus(), "Не верный статус Epic");
    }

    void testEpicSubTasksDoneAndNew() {
        addTasksEpicsSubtasks();
        Status newStatus = DONE;

        taskManager.refreshSubTask(new Subtask(savedSubTaskId2.getId(), "New SubTask2", "Test addNewSubTask2", newStatus, savedEpicId1.getId()));
        savedSubTaskId2 = taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        savedEpicId1 = taskManager.searchEpicForId(savedEpicId1.getId());

        assertEquals(newStatus, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(NEW, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(NEW, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(IN_PROGRESS, savedEpicId1.getStatus(), "Не верный статус Epic");
    }

    void testEpicSubTasksInProgress() {
        addTasksEpicsSubtasks();
        Status newStatus = IN_PROGRESS;

        taskManager.refreshSubTask(new Subtask(savedSubTaskId2.getId(), "New SubTask2", "Test addNewSubTask2", newStatus, savedEpicId1.getId()));
        savedSubTaskId2 = taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        taskManager.refreshSubTask(new Subtask(savedSubTaskId3.getId(), "New SubTask3", "Test addNewSubTask3", newStatus, savedEpicId1.getId()));
        savedSubTaskId3 = taskManager.searchSubtaskForId(savedSubTaskId3.getId());

        savedEpicId1 = taskManager.searchEpicForId(savedEpicId1.getId());

        assertEquals(newStatus, savedSubTaskId2.getStatus(), "Не верный статус savedSubTaskId2");
        assertEquals(newStatus, savedSubTaskId3.getStatus(), "Не верный статус savedSubTaskId3");
        assertEquals(NEW, savedSubTaskId4.getStatus(), "Не верный статус savedSubTaskId4");
        assertEquals(newStatus, savedEpicId1.getStatus(), "Не верный статус Epic");
    }

    void testSearchEpicForId() {
        addTasksEpicsSubtasks();
        boolean isOk = false;
        assertFalse(isOk, "Есть в списке");
        Epic epic = taskManager.searchEpicForId(savedEpicId1.getId());
        isOk = taskManager.getHistory().contains(epic);
        assertTrue(isOk, "Нет с списке");
        assertEquals(savedEpicId1, epic, "Не совпадают Эпики");
    }

    void testSearchEpicForIdClearHash() {
        boolean isOk;
        Epic epic = taskManager.searchEpicForId(1);
        isOk = taskManager.getHistory().contains(epic);
        assertFalse(isOk, "Нет с списке");
        assertNull(epic, "Не null");
    }

    void testSearchEpicForIdIncorrect() {
        addTasksEpicsSubtasks();
        boolean isOk;
        Epic epic = taskManager.searchEpicForId(10);
        isOk = taskManager.getHistory().contains(epic);
        assertFalse(isOk, "Нет с списке");
        assertNull(epic, "Не null");
    }

    void testSearchTaskForId() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        boolean isOk = false;
        assertFalse(isOk, "Есть в списке");
        taskManager.newTask(task);
        Task savedTask = taskManager.searchTaskForId(task.getId());
        isOk = taskManager.getHistory().contains(savedTask);
        assertTrue(isOk, "Нет с списке");
        assertEquals(task, savedTask, "Не совпадают Задачи");
    }

    void testSearchTaskForIdClearHash() {
        boolean isOk = false;
        Task savedTask = taskManager.searchTaskForId(1);
        isOk = taskManager.getHistory().contains(savedTask);
        assertFalse(isOk, "Нет с списке");
        assertNull(savedTask, "Не null");
    }

    void testSearchTaskForIdIncorrect() {
        boolean isOk = false;
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.newTask(task);
        Task savedTask = taskManager.searchTaskForId(67);
        isOk = taskManager.getHistory().contains(savedTask);
        assertFalse(isOk, "Нет с списке");
        assertNull(savedTask, "Не null");
    }

    void testSearchSubTaskForId() {
        addTasksEpicsSubtasks();
        boolean isOk = false;
        assertFalse(isOk, "Есть в списке");
        Task subtask = taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        isOk = taskManager.getHistory().contains(subtask);
        assertTrue(isOk, "Нет с списке");
        assertEquals(savedSubTaskId2, subtask, "Не совпадают Задачи");
    }

    void testSearchSubTaskForIdClearHash() {
        boolean isOk;
        Task subtask = taskManager.searchSubtaskForId(1);
        isOk = taskManager.getHistory().contains(subtask);
        assertFalse(isOk, "Нет с списке");
        assertNull(subtask, "Не null");
    }

    void testSearchSubTaskForIdIncorrect() {
        addTasksEpicsSubtasks();
        boolean isOk;
        Task subtask = taskManager.searchSubtaskForId(10);
        isOk = taskManager.getHistory().contains(subtask);
        assertFalse(isOk, "Нет с списке");
        assertNull(subtask, "Не null");
    }

    void testClearEpic() {
        addTasksEpicsSubtasks();
        taskManager.clearEpic();
        List<Epic> epics = taskManager.getEpics();
        assertTrue(epics.isEmpty(), "Не пустой");
    }

    void testClearTask() {
        addTasksEpicsSubtasks();
        taskManager.clearTask();
        List<Task> tasks = taskManager.getTasks();
        assertTrue(tasks.isEmpty(), "Не пустой");
    }

    void testClearSubTask() {
        addTasksEpicsSubtasks();
        taskManager.clearSubtask();
        List<Subtask> subTasks = taskManager.getSubTasks();
        assertTrue(subTasks.isEmpty(), "Не пустой");
    }

    void testRefreshTask() {
        addTasksEpicsSubtasks();
        Task newTask = new Task(savedTaskId5.getId(), "newTaskName", "newTaskDiscription", IN_PROGRESS);
        taskManager.refreshTask(newTask);
        savedTaskId5 = taskManager.searchTaskForId(savedTaskId5.getId());
        assertEquals(newTask, savedTaskId5, "Задача не обновилась");
    }

    void testRefreshTaskNull() {
        addTasksEpicsSubtasks();
        Task newTask = null;
        taskManager.refreshTask(newTask);
        boolean isOk = taskManager.getTasks().contains(newTask);
        assertFalse(isOk, "Ошибка");
    }

    void testRefreshSubTask() {
        addTasksEpicsSubtasks();
        int subTaskId = savedSubTaskId2.getId();
        int epicId = savedEpicId1.getId();
        Subtask newSubTask = new Subtask(subTaskId, "Test addNewSubTask", "Test addNewSubTask description", IN_PROGRESS, epicId);
        assertEquals(savedEpicId1.getStatus(), NEW, "Статус эпика не NEW");

        taskManager.refreshSubTask(newSubTask);
        savedSubTaskId2 = taskManager.searchSubtaskForId(subTaskId);
        savedEpicId1 = taskManager.searchEpicForId(epicId);
        boolean subTaskInEpicList = savedEpicId1.getEpicSubTasksList().contains(newSubTask);
        assertEquals(newSubTask, savedSubTaskId2, "Задача не обновилась");
        assertTrue(subTaskInEpicList, "Задача не найдена в спике у эпика");
        assertEquals(savedEpicId1.getStatus(), IN_PROGRESS, "Статус эпика не NEW");
    }

    void testRefreshSubTaskNull() {
        addTasksEpicsSubtasks();
        Subtask newSubTask = null;
        assertEquals(savedEpicId1.getStatus(), NEW, "Статус эпика не NEW");

        taskManager.refreshSubTask(newSubTask);
        boolean isOk = taskManager.getSubTasks().contains(newSubTask);
        assertFalse(isOk, "Ошибка");
    }

    void testRefreshEpic() {
        addTasksEpicsSubtasks();
        int epicId = savedEpicId1.getId();
        Epic newEpic = new Epic(epicId, "Test epic", "TestRefreshEpic", savedEpicId1.getStatus());
        taskManager.refreshEpic(newEpic);
        savedEpicId1 = taskManager.searchEpicForId(epicId);
        assertEquals(newEpic, savedEpicId1, "Эпик не обновился");
    }

    void testRefreshEpicNull() {
        addTasksEpicsSubtasks();
        Epic newEpic = null;
        taskManager.refreshEpic(newEpic);
        boolean isOk = taskManager.getEpics().contains(newEpic);
        assertFalse(isOk, "Ошибка");
    }

    void testDeleteTaskForId() {
        addTasksEpicsSubtasks();

        boolean isOkTaskManager = taskManager.getTasks().contains(savedTaskId5);
        taskManager.searchTaskForId(savedTaskId5.getId());
        boolean isOkHistory = taskManager.getHistory().contains(savedTaskId5);
        assertTrue(isOkTaskManager, "Ошибка");
        assertTrue(isOkHistory, "Ошибка");

        taskManager.deleteTaskForId(savedTaskId5.getId());
        isOkTaskManager = taskManager.getTasks().contains(savedTaskId5);
        isOkHistory = taskManager.getHistory().contains(savedTaskId5);
        assertFalse(isOkTaskManager, "Ошибка3");
        assertFalse(isOkHistory, "Ошибка4");
    }

    void testDeleteTaskForIdWithIncorrectId() {
        addTasksEpicsSubtasks();
        int sizeBefore = taskManager.getTasks().size();
        taskManager.deleteTaskForId(100);
        int sizeAfter = taskManager.getTasks().size();
        assertEquals(sizeBefore, sizeAfter, "Размеры не совпадают");
    }

    void testDeleteSubTaskForId() {
        addTasksEpicsSubtasks();

        boolean isOkTaskManager = taskManager.getSubTasks().contains(savedSubTaskId2);
        taskManager.searchSubtaskForId(savedSubTaskId2.getId());
        boolean isOkHistory = taskManager.getHistory().contains(savedSubTaskId2);
        assertTrue(isOkTaskManager, "Ошибка1");
        assertTrue(isOkHistory, "Ошибка2");

        taskManager.deleteSubTaskForId(savedSubTaskId2.getId());
        isOkTaskManager = taskManager.getSubTasks().contains(savedSubTaskId2);
        isOkHistory = taskManager.getHistory().contains(savedSubTaskId2);
        assertFalse(isOkTaskManager, "Ошибка3");
        assertFalse(isOkHistory, "Ошибка4");
    }

    void testDeleteSubTaskForIdWithIncorrectId() {
        addTasksEpicsSubtasks();
        int sizeBefore = taskManager.getSubTasks().size();
        taskManager.deleteSubTaskForId(100);
        int sizeAfter = taskManager.getSubTasks().size();
        assertEquals(sizeBefore, sizeAfter, "Размеры не совпадают");
    }

    void testDeleteEpicForId() {
        addTasksEpicsSubtasks();

        boolean isOkTaskManager = taskManager.getEpics().contains(savedEpicId1);
        taskManager.searchEpicForId(savedEpicId1.getId());
        boolean isOkHistory = taskManager.getHistory().contains(savedEpicId1);
        assertTrue(isOkTaskManager, "Ошибка1");
        assertTrue(isOkHistory, "Ошибка2");

        taskManager.deleteEpicForId(savedEpicId1.getId());
        isOkTaskManager = taskManager.getEpics().contains(savedEpicId1);
        isOkHistory = taskManager.getHistory().contains(savedEpicId1);
        assertFalse(isOkTaskManager, "Ошибка3");
        assertFalse(isOkHistory, "Ошибка4");
    }

    void testDeleteEpicForIdWithIncorrectId() {
        addTasksEpicsSubtasks();
        int sizeBefore = taskManager.getEpics().size();
        taskManager.deleteEpicForId(100);
        int sizeAfter = taskManager.getEpics().size();
        assertEquals(sizeBefore, sizeAfter, "Размеры не совпадают");
    }


    void testGetHistory() {
        Task task = new Task("Test addNewTask1", "Test addNewTask description2", NEW);
        final int taskId = taskManager.newTask(task);
        Epic epic = new Epic("Test addNewEpic", "Epic addNewTask description", NEW);
        final int epicId = taskManager.newEpic(epic);
        Subtask subTask = new Subtask("Test addNewSubTask", "Test addNewSubTask description", NEW, epicId);
        final int subTaskId = taskManager.newSubTask(subTask);

        taskManager.searchTaskForId(taskId);
        taskManager.searchSubtaskForId(subTaskId);
        taskManager.searchEpicForId(epicId);

        List<Task> listTasks = List.of(task, epic, subTask);
        List<Task> listTasksFromTaskManager = taskManager.getHistory();
        assertEquals(listTasks.size(), listTasksFromTaskManager.size(), "Размеры не совпадают");
        boolean isOkHistory = listTasksFromTaskManager.contains(task);
        assertTrue(isOkHistory, "Не найден Task");
        isOkHistory = false;
        isOkHistory = listTasksFromTaskManager.contains(subTask);
        assertTrue(isOkHistory, "Не найден SubTask");
        isOkHistory = false;
        isOkHistory = listTasksFromTaskManager.contains(epic);
        assertTrue(isOkHistory, "Не найден Epic");
    }

    void testGetSubTasksForEpicId() {
        addTasksEpicsSubtasks();
        ArrayList<Subtask> subtasksEpic = savedEpicId1.getEpicSubTasksList();
        ArrayList<Subtask> subtasksTaskManager = taskManager.getSubTasksForEpicId(savedEpicId1.getId());
        assertEquals(subtasksEpic.size(), subtasksTaskManager.size(), "Размеры не совпадают");
        boolean isOk = false;
        for (Subtask subtask : subtasksEpic) {
            isOk = subtasksTaskManager.contains(subtask);
            assertTrue(isOk, "Не найден SubTask");
        }
    }
}
