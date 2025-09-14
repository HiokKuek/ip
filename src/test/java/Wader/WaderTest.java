package wader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WaderTest {

    private Wader wader;
    private String testFilePath;

    @BeforeEach
    public void setUp() {
        testFilePath = "test_wader.txt";
        wader = new Wader(testFilePath);
    }

    @AfterEach
    public void tearDown() {
        // Clean up test file after each test
        try {
            Files.deleteIfExists(Paths.get(testFilePath));
        } catch (IOException e) {
            // Ignore cleanup failures
        }
    }

    @Test
    public void dummyTest() {
        assertEquals(2, 2);
    }

    @Test
    public void anotherDummyTest() {
        assertEquals(3, 3);
    }

    // Test basic command processing
    @Test
    public void getResponse_listCommand_returnsEmptyListMessage() {
        String response = wader.getResponse("list");
        assertTrue(response.contains("no tasks") || response.contains("empty"));
    }

    @Test
    public void getResponse_todoCommand_addsTaskAndReturnsConfirmation() {
        String response = wader.getResponse("todo read book");
        assertTrue(response.contains("read book"));
        assertTrue(response.contains("added") || response.contains("Got it"));
    }

    @Test
    public void getResponse_deadlineCommand_addsTaskAndReturnsConfirmation() {
        String response = wader.getResponse("deadline submit report /by 2025-08-30 18:00");
        assertTrue(response.contains("submit report"));
        assertTrue(response.contains("added") || response.contains("Got it"));
    }

    @Test
    public void getResponse_eventCommand_addsTaskAndReturnsConfirmation() {
        String response = wader.getResponse("event meeting /from 2025-08-30 14:00 /to 2025-08-30 16:00");
        assertTrue(response.contains("meeting"));
        assertTrue(response.contains("added") || response.contains("Got it"));
    }

    @Test
    public void getResponse_markCommand_marksTaskCorrectly() {
        // Add a task first
        wader.getResponse("todo test task");

        // Mark it
        String response = wader.getResponse("mark 1");
        assertTrue(response.contains("marked") || response.contains("done") || response.contains("[X]"));
    }

    @Test
    public void getResponse_unmarkCommand_unmarksTaskCorrectly() {
        // Add and mark a task first
        wader.getResponse("todo test task");
        wader.getResponse("mark 1");

        // Unmark it
        String response = wader.getResponse("unmark 1");
        assertTrue(response.contains("unmarked") || response.contains("[ ]"));
    }

    @Test
    public void getResponse_deleteCommand_deletesTaskCorrectly() {
        // Add a task first
        wader.getResponse("todo test task");

        // Delete it
        String response = wader.getResponse("delete 1");
        assertTrue(response.contains("removed") || response.contains("deleted"));
    }

    @Test
    public void getResponse_findCommand_findsMatchingTasks() {
        // Add some tasks
        wader.getResponse("todo buy milk");
        wader.getResponse("todo read book");
        wader.getResponse("todo buy groceries");

        // Find tasks with "buy"
        String response = wader.getResponse("find buy");
        assertTrue(response.contains("buy milk"));
        assertTrue(response.contains("buy groceries"));
        assertFalse(response.contains("read book"));
    }

    @Test
    public void getResponse_remindCommand_showsUpcomingTasks() {
        // Add a future deadline task
        wader.getResponse("deadline future task /by 2025-12-01 18:00");

        String response = wader.getResponse("remind");
        // Should contain upcoming tasks or a message about reminders
        assertTrue(response.contains("upcoming") || response.contains("reminder") || response.contains("future task"));
    }

    // Test error handling
    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        String response = wader.getResponse("invalid command");
        assertTrue(response.contains("don't know") || response.contains("invalid") || response.contains("OOPS"));
    }

    @Test
    public void getResponse_markInvalidIndex_returnsErrorMessage() {
        String response = wader.getResponse("mark 999");
        assertTrue(response.contains("Invalid") || response.contains("OOPS"));
    }

    @Test
    public void getResponse_deleteInvalidIndex_returnsErrorMessage() {
        String response = wader.getResponse("delete 999");
        assertTrue(response.contains("Invalid") || response.contains("OOPS"));
    }

    @Test
    public void getResponse_todoEmptyDescription_returnsErrorMessage() {
        String response = wader.getResponse("todo");
        assertTrue(response.contains("empty") || response.contains("OOPS"));
    }

    @Test
    public void getResponse_deadlineInvalidFormat_returnsErrorMessage() {
        String response = wader.getResponse("deadline task without deadline");
        assertTrue(response.contains("Invalid") || response.contains("OOPS"));
    }

    @Test
    public void getResponse_eventInvalidFormat_returnsErrorMessage() {
        String response = wader.getResponse("event meeting without time");
        assertTrue(response.contains("Invalid") || response.contains("OOPS"));
    }

    // Integration workflow tests
    @Test
    public void wader_fullWorkflow_worksCorrectly() {
        // Add different types of tasks
        String response1 = wader.getResponse("todo buy groceries");
        assertTrue(response1.contains("buy groceries"));

        String response2 = wader.getResponse("deadline submit assignment /by 2025-08-30 23:59");
        assertTrue(response2.contains("submit assignment"));

        String response3 = wader.getResponse("event team meeting /from 2025-08-30 14:00 /to 2025-08-30 16:00");
        assertTrue(response3.contains("team meeting"));

        // List all tasks
        String listResponse = wader.getResponse("list");
        assertTrue(listResponse.contains("buy groceries"));
        assertTrue(listResponse.contains("submit assignment"));
        assertTrue(listResponse.contains("team meeting"));

        // Mark some tasks
        wader.getResponse("mark 1");
        wader.getResponse("mark 3");

        // Delete a task
        String deleteResponse = wader.getResponse("delete 2");
        assertTrue(deleteResponse.contains("submit assignment"));

        // Verify final state
        String finalList = wader.getResponse("list");
        assertTrue(finalList.contains("buy groceries"));
        assertFalse(finalList.contains("submit assignment")); // Should be deleted
        assertTrue(finalList.contains("team meeting"));
    }

    @Test
    public void wader_invalidCommandSequence_handlesGracefully() {
        // Mix valid and invalid commands
        wader.getResponse("invalid1");
        wader.getResponse("todo valid task");
        wader.getResponse("mark invalid");
        wader.getResponse("mark 1"); // Valid
        wader.getResponse("delete abc");
        wader.getResponse("delete 1"); // Valid

        // Should handle all gracefully without crashing
        String listResponse = wader.getResponse("list");
        assertTrue(listResponse.contains("no tasks") || listResponse.contains("empty"));
    }

    @Test
    public void wader_persistenceTest_savesAndLoadsCorrectly() {
        // Add tasks
        wader.getResponse("todo persistent task");
        wader.getResponse("deadline important /by 2025-12-01 18:00");

        // Mark one task
        wader.getResponse("mark 1");

        // Trigger save (bye command saves)
        wader.getResponse("bye");

        // Create new Wader instance with same file
        Wader newWader = new Wader(testFilePath);

        // Check if tasks were loaded
        String listResponse = newWader.getResponse("list");
        assertTrue(listResponse.contains("persistent task"));
        assertTrue(listResponse.contains("important"));
        // Should show that first task is marked
        assertTrue(listResponse.contains("[X]"));
    }

    @Test
    public void wader_edgeCasesHandling_worksCorrectly() {
        // Test various edge cases

        // Empty input (this might not be handled by getResponse, but testing boundary)
        // wader.getResponse(""); // Might cause assertion error due to precondition

        // Very long task description
        String longDesc = "a".repeat(500);
        String response1 = wader.getResponse("todo " + longDesc);
        assertTrue(response1.contains("added") || response1.contains("Got it"));

        // Special characters in task description
        String response2 = wader.getResponse("todo Task with special chars: !@#$%^&*()");
        assertTrue(response2.contains("added") || response2.contains("Got it"));

        // Multiple spaces in commands
        String response3 = wader.getResponse("todo    task   with   spaces");
        assertTrue(response3.contains("added") || response3.contains("Got it"));

        // Case sensitivity
        String response4 = wader.getResponse("TODO uppercase command");
        // Should be treated as unknown command since commands are case-sensitive
        assertTrue(response4.contains("don't know") || response4.contains("invalid") || response4.contains("OOPS"));
    }

    @Test
    public void wader_stressTest_handlesMultipleTasks() {
        // Add many tasks to test performance
        for (int i = 0; i < 50; i++) {
            String response = wader.getResponse("todo task " + i);
            assertTrue(response.contains("task " + i));
        }

        // List all tasks
        String listResponse = wader.getResponse("list");
        assertTrue(listResponse.contains("task 0"));
        assertTrue(listResponse.contains("task 49"));

        // Mark several tasks
        for (int i = 1; i <= 10; i++) {
            wader.getResponse("mark " + i);
        }

        // Delete some tasks
        for (int i = 0; i < 5; i++) {
            wader.getResponse("delete 1"); // Always delete the first task
        }

        // Verify final state
        String finalList = wader.getResponse("list");
        // Should have 45 tasks remaining (50 - 5 deleted)
        assertTrue(finalList.contains("task"));
    }
}
