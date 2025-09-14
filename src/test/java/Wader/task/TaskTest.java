package wader.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import wader.util.DukeException;

public class TaskTest {

    // Test ToDoTask
    @Test
    public void todoTask_creation_setsPropertiesCorrectly() {
        ToDoTask task = new ToDoTask("read book");

        assertEquals("read book", task.getDescription());
        assertFalse(task.isDone());
        assertFalse(task.hasDate());
        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    public void todoTask_markAndUnmark_changesStatus() {
        ToDoTask task = new ToDoTask("test task");

        assertFalse(task.isDone());
        task.markAsDone();
        assertTrue(task.isDone());
        assertEquals("[T][X] test task", task.toString());

        task.markAsNotDone();
        assertFalse(task.isDone());
        assertEquals("[T][ ] test task", task.toString());
    }

    @Test
    public void todoTask_hasDate_returnsFalse() {
        ToDoTask task = new ToDoTask("test task");
        assertFalse(task.hasDate());
    }

    @Test
    public void todoTask_getDateTime_throwsException() {
        ToDoTask task = new ToDoTask("test task");
        assertThrows(UnsupportedOperationException.class, () -> {
            task.getDateTime();
        });
    }

    @Test
    public void todoTask_emptyDescription_handlesCorrectly() {
        ToDoTask task = new ToDoTask("");
        assertEquals("", task.getDescription());
        assertEquals("[T][ ] ", task.toString());
    }

    @Test
    public void todoTask_longDescription_handlesCorrectly() {
        String longDesc = "a".repeat(1000);
        ToDoTask task = new ToDoTask(longDesc);
        assertEquals(longDesc, task.getDescription());
        assertTrue(task.toString().contains(longDesc));
    }

    @Test
    public void todoTask_specialCharacters_handlesCorrectly() {
        String specialDesc = "Task with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";
        ToDoTask task = new ToDoTask(specialDesc);
        assertEquals(specialDesc, task.getDescription());
        assertTrue(task.toString().contains(specialDesc));
    }

    // Test DeadlineTask
    @Test
    public void deadlineTask_validDateTime_createsCorrectly() throws DukeException {
        DeadlineTask task = new DeadlineTask("submit assignment", "2025-08-30", "18:00");

        assertEquals("submit assignment", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task.hasDate());
        assertTrue(task.toString().contains("submit assignment"));
        assertTrue(task.toString().contains("Aug 30 2025"));
        assertTrue(task.toString().contains("6pm"));
    }

    @Test
    public void deadlineTask_invalidDateFormat_throwsException() {
        assertThrows(Exception.class, () -> {
            new DeadlineTask("task", "invalid-date", "18:00");
        });

        assertThrows(Exception.class, () -> {
            new DeadlineTask("task", "2025-13-01", "18:00"); // Invalid month
        });

        assertThrows(Exception.class, () -> {
            new DeadlineTask("task", "2025-02-30", "18:00"); // Invalid day
        });
    }

    @Test
    public void deadlineTask_invalidTimeFormat_throwsException() {
        assertThrows(Exception.class, () -> {
            new DeadlineTask("task", "2025-08-30", "25:00"); // Invalid hour
        });

        assertThrows(Exception.class, () -> {
            new DeadlineTask("task", "2025-08-30", "12:60"); // Invalid minute
        });

        assertThrows(Exception.class, () -> {
            new DeadlineTask("task", "2025-08-30", "invalid-time");
        });
    }

    @Test
    public void deadlineTask_pastDate_stillAccepted() throws DukeException {
        // Business logic decision: past dates should be accepted
        DeadlineTask task = new DeadlineTask("old task", "2020-01-01", "12:00");

        assertEquals("old task", task.getDescription());
        assertTrue(task.hasDate());
        assertTrue(task.getDateTime().isBefore(LocalDateTime.now()));
    }

    @Test
    public void deadlineTask_getDateTime_returnsCorrectDateTime() throws DukeException {
        DeadlineTask task = new DeadlineTask("test", "2025-08-30", "18:00");

        LocalDateTime dateTime = task.getDateTime();
        assertEquals(2025, dateTime.getYear());
        assertEquals(8, dateTime.getMonthValue());
        assertEquals(30, dateTime.getDayOfMonth());
        assertEquals(18, dateTime.getHour());
        assertEquals(0, dateTime.getMinute());
    }

    @Test
    public void deadlineTask_markAndUnmark_preservesDateTime() throws DukeException {
        DeadlineTask task = new DeadlineTask("test", "2025-08-30", "18:00");
        LocalDateTime originalDateTime = task.getDateTime();

        task.markAsDone();
        assertEquals(originalDateTime, task.getDateTime());

        task.markAsNotDone();
        assertEquals(originalDateTime, task.getDateTime());
    }

    @Test
    public void deadlineTask_edgeDates_handlesCorrectly() throws DukeException {
        // Test edge cases for dates
        DeadlineTask task1 = new DeadlineTask("new year", "2025-01-01", "00:00");
        assertEquals(1, task1.getDateTime().getMonthValue());
        assertEquals(1, task1.getDateTime().getDayOfMonth());

        DeadlineTask task2 = new DeadlineTask("new year eve", "2025-12-31", "23:59");
        assertEquals(12, task2.getDateTime().getMonthValue());
        assertEquals(31, task2.getDateTime().getDayOfMonth());
        assertEquals(23, task2.getDateTime().getHour());
        assertEquals(59, task2.getDateTime().getMinute());
    }

    // Test EventTask
    @Test
    public void eventTask_validDateTime_createsCorrectly() throws DukeException {
        EventTask task = new EventTask("meeting", "14:00", "16:00", "2025-08-30", "2025-08-30");

        assertEquals("meeting", task.getDescription());
        assertFalse(task.isDone());
        assertTrue(task.hasDate());
        assertTrue(task.toString().contains("meeting"));
        assertTrue(task.toString().contains("Aug 30 2025"));
        assertTrue(task.toString().contains("2pm"));
        assertTrue(task.toString().contains("4pm"));
    }

    @Test
    public void eventTask_overlappingFromTo_stillCreates() throws DukeException {
        // Business logic: allow "from" time to be after "to" time
        EventTask task = new EventTask("meeting", "18:00", "14:00", "2025-08-30", "2025-08-30");

        assertEquals("meeting", task.getDescription());
        assertTrue(task.hasDate());
        // Should still create the task even if times seem backwards
    }

    @Test
    public void eventTask_invalidFromTime_throwsException() {
        assertThrows(Exception.class, () -> {
            new EventTask("event", "25:00", "16:00", "2025-08-30", "2025-08-30");
        });

        assertThrows(Exception.class, () -> {
            new EventTask("event", "12:60", "16:00", "2025-08-30", "2025-08-30");
        });

        assertThrows(Exception.class, () -> {
            new EventTask("event", "invalid", "16:00", "2025-08-30", "2025-08-30");
        });
    }

    @Test
    public void eventTask_invalidToTime_throwsException() {
        assertThrows(Exception.class, () -> {
            new EventTask("event", "14:00", "25:00", "2025-08-30", "2025-08-30");
        });

        assertThrows(Exception.class, () -> {
            new EventTask("event", "14:00", "16:60", "2025-08-30", "2025-08-30");
        });
    }

    @Test
    public void eventTask_invalidFromDate_throwsException() {
        assertThrows(Exception.class, () -> {
            new EventTask("event", "14:00", "16:00", "invalid-date", "2025-08-30");
        });

        assertThrows(Exception.class, () -> {
            new EventTask("event", "14:00", "16:00", "2025-13-01", "2025-08-30");
        });
    }

    @Test
    public void eventTask_invalidToDate_throwsException() {
        assertThrows(Exception.class, () -> {
            new EventTask("event", "14:00", "16:00", "2025-08-30", "invalid-date");
        });

        assertThrows(Exception.class, () -> {
            new EventTask("event", "14:00", "16:00", "2025-08-30", "2025-02-30");
        });
    }

    @Test
    public void eventTask_getDateTime_returnsFromDateTime() throws DukeException {
        EventTask task = new EventTask("event", "14:00", "16:00", "2025-08-30", "2025-08-31");

        LocalDateTime dateTime = task.getDateTime();
        // Should return the "from" datetime for sorting purposes
        assertEquals(2025, dateTime.getYear());
        assertEquals(8, dateTime.getMonthValue());
        assertEquals(30, dateTime.getDayOfMonth());
        assertEquals(14, dateTime.getHour());
        assertEquals(0, dateTime.getMinute());
    }

    @Test
    public void eventTask_spanningMultipleDays_handlesCorrectly() throws DukeException {
        EventTask task = new EventTask("conference", "09:00", "17:00", "2025-08-30", "2025-09-02");

        assertTrue(task.hasDate());
        assertTrue(task.toString().contains("Aug 30 2025"));
        assertTrue(task.toString().contains("Sep 2 2025"));
    }

    @Test
    public void eventTask_markAndUnmark_preservesDateTime() throws DukeException {
        EventTask task = new EventTask("event", "14:00", "16:00", "2025-08-30", "2025-08-30");
        LocalDateTime originalDateTime = task.getDateTime();

        task.markAsDone();
        assertEquals(originalDateTime, task.getDateTime());

        task.markAsNotDone();
        assertEquals(originalDateTime, task.getDateTime());
    }

    // Comparative tests between task types
    @Test
    public void tasks_comparison_dateTimeOrdering() throws DukeException {
        DeadlineTask deadline1 = new DeadlineTask("deadline1", "2025-08-30", "18:00");
        DeadlineTask deadline2 = new DeadlineTask("deadline2", "2025-08-31", "18:00");
        EventTask event = new EventTask("event", "14:00", "16:00", "2025-08-30", "2025-08-30");

        // Compare date times for sorting
        assertTrue(deadline1.getDateTime().isBefore(deadline2.getDateTime()));
        assertTrue(event.getDateTime().isBefore(deadline1.getDateTime())); // Same day but earlier time
    }

    @Test
    public void tasks_toString_formatConsistency() throws DukeException {
        ToDoTask todo = new ToDoTask("todo task");
        DeadlineTask deadline = new DeadlineTask("deadline task", "2025-08-30", "18:00");
        EventTask event = new EventTask("event task", "14:00", "16:00", "2025-08-30", "2025-08-30");

        // All should start with [T], [D], or [E] followed by [ ] or [X]
        assertTrue(todo.toString().matches("\\[T\\]\\[ \\].*"));
        assertTrue(deadline.toString().matches("\\[D\\]\\[ \\].*"));
        assertTrue(event.toString().matches("\\[E\\]\\[ \\].*"));

        // Mark tasks and check format consistency
        todo.markAsDone();
        deadline.markAsDone();
        event.markAsDone();

        assertTrue(todo.toString().matches("\\[T\\]\\[X\\].*"));
        assertTrue(deadline.toString().matches("\\[D\\]\\[X\\].*"));
        assertTrue(event.toString().matches("\\[E\\]\\[X\\].*"));
    }

    @Test
    public void tasks_polymorphism_worksCorrectly() throws DukeException {
        // Test that Task references work correctly with subclasses
        Task[] tasks = {
                new ToDoTask("todo"),
                new DeadlineTask("deadline", "2025-08-30", "18:00"),
                new EventTask("event", "14:00", "16:00", "2025-08-30", "2025-08-30")
        };

        for (Task task : tasks) {
            assertFalse(task.isDone());
            task.markAsDone();
            assertTrue(task.isDone());
            assertTrue(task.toString().contains("[X]"));
        }

        // Check hasDate() behavior
        assertFalse(tasks[0].hasDate()); // ToDoTask
        assertTrue(tasks[1].hasDate()); // DeadlineTask
        assertTrue(tasks[2].hasDate()); // EventTask
    }
}
