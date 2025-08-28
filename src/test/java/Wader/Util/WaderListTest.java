package Wader.Util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import Wader.Task.Task;
import Wader.Task.ToDoTask;
import Wader.Task.DeadlineTask;
import Wader.Task.EventTask;

public class WaderListTest {

    private WaderList waderList;

    @BeforeEach
    public void setUp() {
        waderList = new WaderList();
    }

    // Test constructor and initial state
    @Test
    public void constructor_newList_isEmpty() {
        assertTrue(waderList.isEmpty());
        assertEquals(0, waderList.getSize());
    }

    // Test addToDoTask() method
    @Test
    public void addToDoTask_validDescription_addsTask() {
        Task task = waderList.addToDoTask("read book");

        assertFalse(waderList.isEmpty());
        assertEquals(1, waderList.getSize());
        assertEquals("read book", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task instanceof ToDoTask);
    }

    @Test
    public void addToDoTask_emptyDescription_addsTaskWithEmptyDescription() {
        Task task = waderList.addToDoTask("");

        assertEquals(1, waderList.getSize());
        assertEquals("", task.getDescription());
    }

    @Test
    public void addToDoTask_multipleTasksDescriptions_addsAllTasks() {
        waderList.addToDoTask("task 1");
        waderList.addToDoTask("task 2");
        waderList.addToDoTask("task 3");

        assertEquals(3, waderList.getSize());
        assertEquals("task 1", waderList.getTasks().get(0).getDescription());
        assertEquals("task 2", waderList.getTasks().get(1).getDescription());
        assertEquals("task 3", waderList.getTasks().get(2).getDescription());
    }

    // Test addDeadlineTask() method
    @Test
    public void addDeadlineTask_validInput_addsTask() throws DukeException {
        Task task = waderList.addDeadlineTask("submit report", "2025-08-30 18:00");

        assertEquals(1, waderList.getSize());
        assertEquals("submit report", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task instanceof DeadlineTask);
    }

    @Test
    public void addDeadlineTask_invalidDateFormat_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("submit report", "invalid-date");
        });
        assertEquals("Invalid deadline format. Please use 'date time' format.", exception.getMessage());
        assertEquals(0, waderList.getSize()); // Task should not be added
    }

    @Test
    public void addDeadlineTask_missingTime_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("submit report", "2025-08-30");
        });
        assertEquals("Invalid deadline format. Please use 'date time' format.", exception.getMessage());
    }

    @Test
    public void addDeadlineTask_emptyDeadline_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("submit report", "");
        });
        assertEquals("Invalid deadline format. Please use 'date time' format.", exception.getMessage());
    }

    // Test addEventTask() method
    @Test
    public void addEventTask_validInput_addsTask() throws DukeException {
        Task task = waderList.addEventTask("meeting", "2025-08-30 14:00", "2025-08-30 16:00");

        assertEquals(1, waderList.getSize());
        assertEquals("meeting", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task instanceof EventTask);
    }

    @Test
    public void addEventTask_invalidFromFormat_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            waderList.addEventTask("meeting", "invalid-from", "2025-08-30 16:00");
        });
        assertEquals("Invalid event format. Please use 'date time' format.", exception.getMessage());
        assertEquals(0, waderList.getSize());
    }

    @Test
    public void addEventTask_invalidToFormat_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            waderList.addEventTask("meeting", "2025-08-30 14:00", "invalid-to");
        });
        assertEquals("Invalid event format. Please use 'date time' format.", exception.getMessage());
        assertEquals(0, waderList.getSize());
    }

    @Test
    public void addEventTask_missingTimeInFrom_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            waderList.addEventTask("meeting", "2025-08-30", "2025-08-30 16:00");
        });
        assertEquals("Invalid event format. Please use 'date time' format.", exception.getMessage());
    }

    // Test delete() method
    @Test
    public void delete_validIndex_removesAndReturnsTask() throws DukeException {
        waderList.addToDoTask("task 1");
        Task task2 = waderList.addToDoTask("task 2");
        waderList.addToDoTask("task 3");

        Task deletedTask = waderList.delete(1); // Delete middle task

        assertEquals(2, waderList.getSize());
        assertEquals(task2, deletedTask);
        assertEquals("task 1", waderList.getTasks().get(0).getDescription());
        assertEquals("task 3", waderList.getTasks().get(1).getDescription());
    }

    @Test
    public void delete_firstIndex_removesFirstTask() {
        waderList.addToDoTask("task 1");
        waderList.addToDoTask("task 2");

        Task deletedTask = waderList.delete(0);

        assertEquals(1, waderList.getSize());
        assertEquals("task 1", deletedTask.getDescription());
        assertEquals("task 2", waderList.getTasks().get(0).getDescription());
    }

    @Test
    public void delete_lastIndex_removesLastTask() {
        waderList.addToDoTask("task 1");
        waderList.addToDoTask("task 2");

        Task deletedTask = waderList.delete(1);

        assertEquals(1, waderList.getSize());
        assertEquals("task 2", deletedTask.getDescription());
        assertEquals("task 1", waderList.getTasks().get(0).getDescription());
    }

    @Test
    public void delete_invalidIndex_throwsIndexOutOfBoundsException() {
        waderList.addToDoTask("task 1");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            waderList.delete(5);
        });
        assertEquals(1, waderList.getSize()); // List should remain unchanged
    }

    @Test
    public void delete_negativeIndex_throwsIndexOutOfBoundsException() {
        waderList.addToDoTask("task 1");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            waderList.delete(-1);
        });
        assertEquals(1, waderList.getSize());
    }

    @Test
    public void delete_emptyList_throwsIndexOutOfBoundsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            waderList.delete(0);
        });
    }

    // Test mark() method
    @Test
    public void mark_validIndex_marksTaskAndReturnsTrue() {
        Task task = waderList.addToDoTask("task 1");

        boolean result = waderList.mark(0);

        assertTrue(result);
        assertTrue(task.isDone());
    }

    @Test
    public void mark_invalidIndex_returnsFalse() {
        waderList.addToDoTask("task 1");

        boolean result = waderList.mark(5);

        assertFalse(result);
    }

    @Test
    public void mark_negativeIndex_returnsFalse() {
        waderList.addToDoTask("task 1");

        boolean result = waderList.mark(-1);

        assertFalse(result);
    }

    @Test
    public void mark_emptyList_returnsFalse() {
        boolean result = waderList.mark(0);

        assertFalse(result);
    }

    @Test
    public void mark_alreadyMarkedTask_remainsMarked() {
        Task task = waderList.addToDoTask("task 1");
        task.markAsDone();

        boolean result = waderList.mark(0);

        assertTrue(result);
        assertTrue(task.isDone());
    }

    // Test unmark() method
    @Test
    public void unmark_markedTask_unmarksTaskAndReturnsTrue() {
        Task task = waderList.addToDoTask("task 1");
        task.markAsDone();

        boolean result = waderList.unmark(0);

        assertTrue(result);
        assertFalse(task.isDone());
    }

    @Test
    public void unmark_unmarkedTask_remainsUnmarked() {
        Task task = waderList.addToDoTask("task 1");

        boolean result = waderList.unmark(0);

        assertTrue(result);
        assertFalse(task.isDone());
    }

    @Test
    public void unmark_invalidIndex_returnsFalse() {
        waderList.addToDoTask("task 1");

        boolean result = waderList.unmark(5);

        assertFalse(result);
    }

    @Test
    public void unmark_emptyList_returnsFalse() {
        boolean result = waderList.unmark(0);

        assertFalse(result);
    }

    // Test getTasks() method
    @Test
    public void getTasks_emptyList_returnsEmptyList() {
        assertTrue(waderList.getTasks().isEmpty());
    }

    @Test
    public void getTasks_withTasks_returnsNewListCopy() throws DukeException {
        waderList.addToDoTask("task 1");
        waderList.addDeadlineTask("task 2", "2025-08-30 18:00");

        var tasks = waderList.getTasks();

        assertEquals(2, tasks.size());
        assertEquals("task 1", tasks.get(0).getDescription());
        assertEquals("task 2", tasks.get(1).getDescription());

        // Verify it's a copy (modifying returned list shouldn't affect original)
        tasks.clear();
        assertEquals(2, waderList.getSize()); // Original list should be unchanged
    }

    // Test getTaskString() method
    @Test
    public void getTaskString_validIndex_returnsTaskString() {
        Task task = waderList.addToDoTask("read book");

        String taskString = waderList.getTaskString(0);

        assertEquals(task.toString(), taskString);
    }

    @Test
    public void getTaskString_invalidIndex_throwsIndexOutOfBoundsException() {
        waderList.addToDoTask("task 1");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            waderList.getTaskString(5);
        });
    }

    // Test getSize() method
    @Test
    public void getSize_emptyList_returnsZero() {
        assertEquals(0, waderList.getSize());
    }

    @Test
    public void getSize_withTasks_returnsCorrectSize() throws DukeException {
        waderList.addToDoTask("task 1");
        assertEquals(1, waderList.getSize());

        waderList.addDeadlineTask("task 2", "2025-08-30 18:00");
        assertEquals(2, waderList.getSize());

        waderList.addEventTask("task 3", "2025-08-30 14:00", "2025-08-30 16:00");
        assertEquals(3, waderList.getSize());

        waderList.delete(1);
        assertEquals(2, waderList.getSize());
    }

    // Test isEmpty() method
    @Test
    public void isEmpty_newList_returnsTrue() {
        assertTrue(waderList.isEmpty());
    }

    @Test
    public void isEmpty_withTasks_returnsFalse() {
        waderList.addToDoTask("task 1");
        assertFalse(waderList.isEmpty());
    }

    @Test
    public void isEmpty_afterDeletingAllTasks_returnsTrue() {
        waderList.addToDoTask("task 1");
        waderList.addToDoTask("task 2");

        waderList.delete(0);
        waderList.delete(0);

        assertTrue(waderList.isEmpty());
    }

    // Integration tests combining multiple operations
    @Test
    public void integrationTest_addMarkDeleteOperations() throws DukeException {
        // Add different types of tasks
        waderList.addToDoTask("todo task");
        waderList.addDeadlineTask("deadline task", "2025-08-30 18:00");
        waderList.addEventTask("event task", "2025-08-30 14:00", "2025-08-30 16:00");

        assertEquals(3, waderList.getSize());

        // Mark some tasks
        assertTrue(waderList.mark(0));
        assertTrue(waderList.mark(2));

        // Verify marking
        assertTrue(waderList.getTasks().get(0).isDone());
        assertFalse(waderList.getTasks().get(1).isDone());
        assertTrue(waderList.getTasks().get(2).isDone());

        // Delete middle task
        Task deletedTask = waderList.delete(1);
        assertEquals("deadline task", deletedTask.getDescription());
        assertEquals(2, waderList.getSize());

        // Verify remaining tasks
        assertEquals("todo task", waderList.getTasks().get(0).getDescription());
        assertEquals("event task", waderList.getTasks().get(1).getDescription());

        // Unmark a task
        assertTrue(waderList.unmark(0));
        assertFalse(waderList.getTasks().get(0).isDone());
    }
}
