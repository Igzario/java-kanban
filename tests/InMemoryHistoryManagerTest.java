import managers.InMemoryHistoryManager;
import tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import static tasks.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    List<Task> listTasks;
    Task task1;
    Task task2;
    Task task3;
    Task task4;

    @Test
    void linkLast() {
        add();
    }

    @Test
    void getTasks() {
        getHistory();
    }

    @Test
    void removeNode() {
        remove();
    }

    @BeforeEach
    @Test
    void add() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2023, 9, 4, 21, 0);
        Duration duration1 = Duration.ofMinutes(65);
        task1 = new Task(1, "Test addNewTask1", "Test addNewTask description1", NEW, localDateTime1, duration1);

        LocalDateTime localDateTime2 = LocalDateTime.of(2022, 9, 4, 21, 0);
        Duration duration2 = Duration.ofMinutes(65);
        task2 = new Task(2, "Test addNewTask2", "Test addNewTask description2", NEW, localDateTime2, duration2);

        LocalDateTime localDateTime3 = LocalDateTime.of(2021, 9, 4, 21, 0);
        Duration duration3 = Duration.ofMinutes(65);
        task3 = new Task(3, "Test addNewTask1", "Test addNewTask description2", NEW, localDateTime3, duration3);

        LocalDateTime localDateTime4 = LocalDateTime.of(2019, 9, 4, 21, 0);
        Duration duration4 = Duration.ofMinutes(65);
        task4 = new Task(4, "Test addNewTask1", "Test addNewTask description2", NEW, localDateTime4, duration4);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        listTasks = historyManager.getHistory();
        assertTrue(historyManager.getHistory().contains(task1), "Задача не добавилась");
        assertTrue(historyManager.getHistory().contains(task2), "Задача не добавилась");
        assertTrue(historyManager.getHistory().contains(task3), "Задача не добавилась");
        assertTrue(historyManager.getHistory().contains(task4), "Задача не добавилась");
        assertEquals(4, historyManager.getHistory().size(), "Размер истории не совпадает");

        historyManager.add(task1);
        historyManager.add(task3);
        assertTrue(historyManager.getHistory().contains(task1), "Задача не добавилась");
        assertEquals(4, historyManager.getHistory().size(), "Произшло задвоение задачи");
    }

    @Test
    void remove() {
        historyManager.remove(task1.getId());
        listTasks = historyManager.getHistory();
        assertFalse(historyManager.getHistory().contains(task1), "Задача не удалилась");
        assertEquals(3, historyManager.getHistory().size(), "Размер истории не совпадает");
        assertEquals(3, listTasks.size(), "Размер истории не совпадает");

        historyManager.remove(task2.getId());
        listTasks = historyManager.getHistory();
        assertFalse(historyManager.getHistory().contains(task2), "Задача не удалилась");
        assertEquals(2, historyManager.getHistory().size(), "Размер истории не совпадает");
        assertEquals(2, listTasks.size(), "Размер истории не совпадает");

        historyManager.remove(task3.getId());
        listTasks = historyManager.getHistory();
        assertFalse(historyManager.getHistory().contains(task3), "Задача не удалилась");
        assertEquals(1, historyManager.getHistory().size(), "Размер истории не совпадает");
        assertEquals(1, listTasks.size(), "Размер истории не совпадает");

        historyManager.remove(task4.getId());
        listTasks = historyManager.getHistory();
        assertFalse(historyManager.getHistory().contains(task4), "Задача не удалилась");
        assertEquals(0, historyManager.getHistory().size(), "Размер истории не совпадает");
        assertEquals(0, listTasks.size(), "Размер истории не совпадает");
    }

    @Test
    void getHistory() {
        listTasks = historyManager.getHistory();
        remove();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        listTasks = historyManager.getHistory();
        assertEquals(task1, historyManager.getHistory().get(task1.getId() - 1), "Задачи не совпадают");
        assertEquals(task1, listTasks.get(task1.getId() - 1), "Задачи не совпадают");

        assertEquals(task2, historyManager.getHistory().get(task2.getId() - 1), "Задачи не совпадают");
        assertEquals(task2, listTasks.get(task2.getId() - 1), "Задачи не совпадают");

        assertEquals(task3, historyManager.getHistory().get(task3.getId() - 1), "Задачи не совпадают");
        assertEquals(task3, listTasks.get(task3.getId() - 1), "Задачи не совпадают");

        assertEquals(task4, historyManager.getHistory().get(task4.getId() - 1), "Задачи не совпадают");
        assertEquals(task4, listTasks.get(task4.getId() - 1), "Задачи не совпадают");
    }
}