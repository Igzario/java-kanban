package Tests;

import Tasks.Epic;
import Tasks.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static Tasks.Status.NEW;

public class EpicTest {

    Epic epic;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    ArrayList<Subtask> list;

    @BeforeEach
    public void addEpicAndSubtask() {
        epic = new Epic(1, "test", "test", NEW);
        subtask1 = new Subtask(2, "New SubTask1", "Test addNewSubTask1", NEW, 1);
        subtask2 = new Subtask(3, "New SubTask2", "Test addNewSubTask2", NEW, 1);
        subtask3 = new Subtask(3, "New SubTask3", "Test addNewSubTask3", NEW, 1);
        list = new ArrayList<>();
        list.add(subtask1);
        list.add(subtask2);
        list.add(subtask3);
        epic.setEpicSubTasksList(list);
        epic.refreshStatusAndTime();
    }

    @Test
    public void testEpic() {
        list.clear();
        epic.setEpicSubTasksList(list);
        epic.refreshStatusAndTime();

    }


}
