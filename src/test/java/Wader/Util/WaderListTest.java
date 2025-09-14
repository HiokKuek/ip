package wader.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import wader.task.DeadlineTask;
import wader.task.EventTask;
import wader.task.Task;
import wader.task.ToDoTask;

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

    // Test findTasks() method
    @Test
    public void findTasks_emptyKeyword_returnsAllTasks() throws DukeException {
        waderList.addToDoTask("buy milk");
        waderList.addToDoTask("read book");
        waderList.addDeadlineTask("submit assignment", "2025-08-30 18:00");

        var foundTasks = waderList.findTasks("");

        assertEquals(3, foundTasks.size()); // Empty keyword should return all tasks
    }

    @Test
    public void findTasks_validKeyword_returnsMatchingTasks() throws DukeException {
        waderList.addToDoTask("buy milk");
        waderList.addToDoTask("read book");
        waderList.addToDoTask("buy groceries");
        waderList.addDeadlineTask("submit assignment", "2025-08-30 18:00");

        var foundTasks = waderList.findTasks("buy");

        assertEquals(2, foundTasks.size());
        assertEquals("buy milk", foundTasks.get(0).getDescription());
        assertEquals("buy groceries", foundTasks.get(1).getDescription());
    }

    @Test
    public void findTasks_noMatches_returnsEmptyList() throws DukeException {
        waderList.addToDoTask("buy milk");
        waderList.addToDoTask("read book");

        var foundTasks = waderList.findTasks("xyz");

        assertTrue(foundTasks.isEmpty());
    }

    @Test
    public void findTasks_caseSensitive_matchesExactCase() throws DukeException {
        waderList.addToDoTask("Buy Milk");
        waderList.addToDoTask("buy milk");

        var foundTasks = waderList.findTasks("Buy");

        assertEquals(1, foundTasks.size());
        assertEquals("Buy Milk", foundTasks.get(0).getDescription());
    }

    @Test
    public void findTasks_partialMatch_returnsMatches() throws DukeException {
        waderList.addToDoTask("assignment submission");
        waderList.addToDoTask("submit report");
        waderList.addToDoTask("read submission guidelines");

        var foundTasks = waderList.findTasks("submission");

        assertEquals(2, foundTasks.size());
        assertEquals("assignment submission", foundTasks.get(0).getDescription());
        assertEquals("read submission guidelines", foundTasks.get(1).getDescription());
    }

    // Test getNextUpcomingTasks() method
    @Test
    public void getNextUpcomingTasks_allTasksPast_returnsEmpty() throws DukeException {
        // Add tasks with past dates
        waderList.addDeadlineTask("old deadline", "2020-01-01 12:00");
        waderList.addEventTask("old event", "2020-01-01 10:00", "2020-01-01 11:00");

        var upcomingTasks = waderList.getNextUpcomingTasks(3);

        assertTrue(upcomingTasks.isEmpty());
    }

    @Test
    public void getNextUpcomingTasks_moreThanRequested_returnsLimitedResults() throws DukeException {
        // Add tasks with future dates
        waderList.addDeadlineTask("deadline 1", "2025-12-01 12:00");
        waderList.addDeadlineTask("deadline 2", "2025-12-02 12:00");
        waderList.addDeadlineTask("deadline 3", "2025-12-03 12:00");
        waderList.addDeadlineTask("deadline 4", "2025-12-04 12:00");
        waderList.addEventTask("event 1", "2025-12-05 10:00", "2025-12-05 11:00");

        var upcomingTasks = waderList.getNextUpcomingTasks(3);

        assertEquals(3, upcomingTasks.size()); // Should limit to 3 even though we have 5
    }

    @Test
    public void getNextUpcomingTasks_mixedTaskTypes_returnsOnlyDatedTasks() throws DukeException {
        waderList.addToDoTask("todo without date");
        waderList.addDeadlineTask("future deadline", "2025-12-01 12:00");
        waderList.addToDoTask("another todo");
        waderList.addEventTask("future event", "2025-12-02 10:00", "2025-12-02 11:00");

        var upcomingTasks = waderList.getNextUpcomingTasks(5);

        assertEquals(2, upcomingTasks.size()); // Only deadline and event should be returned
        // Should be sorted by date
        assertTrue(upcomingTasks.get(0).hasDate());
        assertTrue(upcomingTasks.get(1).hasDate());
    }

    @Test
    public void getNextUpcomingTasks_sortedByDate_returnsInOrder() throws DukeException {
        // Add tasks in random order
        waderList.addDeadlineTask("later deadline", "2025-12-03 12:00");
        waderList.addDeadlineTask("earlier deadline", "2025-12-01 12:00");
        waderList.addEventTask("middle event", "2025-12-02 10:00", "2025-12-02 11:00");

        var upcomingTasks = waderList.getNextUpcomingTasks(3);

        assertEquals(3, upcomingTasks.size());
        // Should be sorted by date (earliest first)
        assertEquals("earlier deadline", upcomingTasks.get(0).getDescription());
        assertEquals("middle event", upcomingTasks.get(1).getDescription());
        assertEquals("later deadline", upcomingTasks.get(2).getDescription());
    }

    @Test
    public void getNextUpcomingTasks_emptyList_returnsEmpty() {
        var upcomingTasks = waderList.getNextUpcomingTasks(3);

        assertTrue(upcomingTasks.isEmpty());
    }

    @Test
    public void getNextUpcomingTasks_zeroCount_returnsEmpty() throws DukeException {
        waderList.addDeadlineTask("future deadline", "2025-12-01 12:00");

        var upcomingTasks = waderList.getNextUpcomingTasks(0);

        assertTrue(upcomingTasks.isEmpty());
    }

    // Boundary condition tests
    @Test
    public void addDeadlineTask_variousInvalidDateFormats_throwsDukeException() {
        // Test various invalid date formats
        assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("task", "invalid-date-format");
        });

        assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("task", "2025-13-01 12:00"); // Invalid month
        });

        assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("task", "2025-02-30 12:00"); // Invalid day
        });

        assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("task", "2025-08-30 25:00"); // Invalid hour
        });

        assertThrows(DukeException.class, () -> {
            waderList.addDeadlineTask("task", "2025-08-30 12:60"); // Invalid minute
        });
    }

    @Test
    public void addEventTask_invalidTimeFormat_throwsDukeException() {
        // Test various invalid time formats
        assertThrows(DukeException.class, () -> {
            waderList.addEventTask("event", "invalid-from", "2025-08-30 16:00");
        });

        assertThrows(DukeException.class, () -> {
            waderList.addEventTask("event", "2025-08-30 14:00", "invalid-to");
        });

        assertThrows(DukeException.class, () -> {
            waderList.addEventTask("event", "2025-08-30 25:00", "2025-08-30 16:00"); // Invalid hour
        });
    }

    // Stress tests
    @Test
    public void addTasks_largeNumber_handlesCorrectly() throws DukeException {
        // Add a large number of tasks to test performance and memory
        for (int i = 0; i < 1000; i++) {
            waderList.addToDoTask("task " + i);
        }

        assertEquals(1000, waderList.getSize());
        assertEquals("task 0", waderList.getTasks().get(0).getDescription());
        assertEquals("task 999", waderList.getTasks().get(999).getDescription());
    }

    @Test
    public void findTasks_largeList_performsEfficiently() throws DukeException {
        // Add many tasks and test search performance
        for (int i = 0; i < 100; i++) {
            waderList.addToDoTask("task " + i);
            waderList.addToDoTask("special task " + i);
        }

        var foundTasks = waderList.findTasks("special");

        assertEquals(100, foundTasks.size());
        for (int i = 0; i < 100; i++) {
            assertTrue(foundTasks.get(i).getDescription().contains("special"));
        }
    }

    // Edge cases for task operations
    @Test
    public void addToDoTask_veryLongDescription_handlesCorrectly() {
        String longDescription = "a".repeat(10000); // Very long description
        var task = waderList.addToDoTask(longDescription);

        assertEquals(longDescription, task.getDescription());
        assertEquals(1, waderList.getSize());
    }

    @Test
    public void addToDoTask_specialCharacters_handlesCorrectly() {
        String specialDesc = "Task with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        var task = waderList.addToDoTask(specialDesc);

        assertEquals(specialDesc, task.getDescription());
        assertEquals(1, waderList.getSize());
    }

    // Additional integration tests
    @Test
    public void waderList_concurrentMarkUnmark_maintainsConsistency() throws DukeException {
        waderList.addToDoTask("task 1");
        waderList.addToDoTask("task 2");
        waderList.addToDoTask("task 3");

        // Test multiple mark/unmark operations
        assertTrue(waderList.mark(0));
        assertTrue(waderList.mark(1));
        assertTrue(waderList.mark(2));

        // All should be marked
        assertTrue(waderList.getTasks().get(0).isDone());
        assertTrue(waderList.getTasks().get(1).isDone());
        assertTrue(waderList.getTasks().get(2).isDone());

        // Unmark some
        assertTrue(waderList.unmark(1));

        // Check final state
        assertTrue(waderList.getTasks().get(0).isDone());
        assertFalse(waderList.getTasks().get(1).isDone());
        assertTrue(waderList.getTasks().get(2).isDone());
    }

    @Test
    public void waderList_addDeleteMarkCycle_worksCorrectly() throws DukeException {
        // Test complex workflow
        waderList.addToDoTask("task 1");
        waderList.addDeadlineTask("task 2", "2025-08-30 18:00");
        waderList.addEventTask("task 3", "2025-08-30 14:00", "2025-08-30 16:00");

        // Mark and delete operations
        waderList.mark(1);
        var deletedTask = waderList.delete(0); // Delete first task
        assertEquals("task 1", deletedTask.getDescription());

        // After deletion, indices shift
        assertEquals(2, waderList.getSize());
        assertTrue(waderList.getTasks().get(0).isDone()); // What was task 2 is now at index 0
        assertEquals("task 2", waderList.getTasks().get(0).getDescription());

        // Add more tasks after deletion
        waderList.addToDoTask("task 4");
        assertEquals(3, waderList.getSize());
        assertEquals("task 4", waderList.getTasks().get(2).getDescription());
    }
}
