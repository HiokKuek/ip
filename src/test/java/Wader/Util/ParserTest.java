package wader.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;



public class ParserTest {

    @Test
    public void parse_byeCommand_returnsByeCommandType() {
        Parser.Command command = Parser.parse("bye");
        assertEquals(Parser.CommandType.BYE, command.getType());
        assertEquals("bye", command.getFullCommand());
    }

    @Test
    public void parse_listCommand_returnsListCommandType() {
        Parser.Command command = Parser.parse("list");
        assertEquals(Parser.CommandType.LIST, command.getType());
        assertEquals("list", command.getFullCommand());
    }

    @Test
    public void parse_markCommand_returnsMarkCommandType() {
        Parser.Command command = Parser.parse("mark 1");
        assertEquals(Parser.CommandType.MARK, command.getType());
        assertEquals("mark 1", command.getFullCommand());
    }

    @Test
    public void parse_unmarkCommand_returnsUnmarkCommandType() {
        Parser.Command command = Parser.parse("unmark 2");
        assertEquals(Parser.CommandType.UNMARK, command.getType());
        assertEquals("unmark 2", command.getFullCommand());
    }

    @Test
    public void parse_todoCommand_returnsTodoCommandType() {
        Parser.Command command = Parser.parse("todo read book");
        assertEquals(Parser.CommandType.TODO, command.getType());
        assertEquals("todo read book", command.getFullCommand());
    }

    @Test
    public void parse_deadlineCommand_returnsDeadlineCommandType() {
        Parser.Command command = Parser.parse("deadline submit report /by 2025-08-30 18:00");
        assertEquals(Parser.CommandType.DEADLINE, command.getType());
        assertEquals("deadline submit report /by 2025-08-30 18:00", command.getFullCommand());
    }

    @Test
    public void parse_eventCommand_returnsEventCommandType() {
        Parser.Command command = Parser.parse("event meeting /from 2025-08-30 14:00 /to 2025-08-30 16:00");
        assertEquals(Parser.CommandType.EVENT, command.getType());
        assertEquals("event meeting /from 2025-08-30 14:00 /to 2025-08-30 16:00", command.getFullCommand());
    }

    @Test
    public void parse_deleteCommand_returnsDeleteCommandType() {
        Parser.Command command = Parser.parse("delete 3");
        assertEquals(Parser.CommandType.DELETE, command.getType());
        assertEquals("delete 3", command.getFullCommand());
    }

    @Test
    public void parse_unknownCommand_returnsUnknownCommandType() {
        Parser.Command command = Parser.parse("invalid command");
        assertEquals(Parser.CommandType.UNKNOWN, command.getType());
        assertEquals("invalid command", command.getFullCommand());
    }

    @Test
    public void parse_commandWithWhitespace_handlesCorrectly() {
        Parser.Command command = Parser.parse("  todo   read book  ");
        assertEquals(Parser.CommandType.TODO, command.getType());
        assertEquals("todo   read book", command.getFullCommand());
    }

    // Test parseTaskIndex() method
    @Test
    public void parseTaskIndex_validInput_returnsZeroBasedIndex() throws DukeException {
        int index = Parser.parseTaskIndex("mark 5", "mark");
        assertEquals(4, index); // 5 - 1 = 4 (0-based)
    }

    @Test
    public void parseTaskIndex_missingNumber_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseTaskIndex("mark", "mark");
        });
        assertEquals("Please provide a task number for mark.", exception.getMessage());
    }

    @Test
    public void parseTaskIndex_invalidNumber_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseTaskIndex("mark abc", "mark");
        });
        assertEquals("Invalid task number format.", exception.getMessage());
    }

    @Test
    public void parseTaskIndex_negativeNumber_returnsNegativeIndex() throws DukeException {
        int index = Parser.parseTaskIndex("mark -1", "mark");
        assertEquals(-2, index); // -1 - 1 = -2
    }

    // Test parseTodoDescription() method
    @Test
    public void parseTodoDescription_validInput_returnsDescription() throws DukeException {
        String description = Parser.parseTodoDescription("todo read book");
        assertEquals("read book", description);
    }

    @Test
    public void parseTodoDescription_emptyDescription_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseTodoDescription("todo");
        });
        assertEquals("OOPS!!! The descripion of a todo cannot be empty.", exception.getMessage());
    }

    @Test
    public void parseTodoDescription_whitespaceOnly_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseTodoDescription("todo   ");
        });
        assertEquals("OOPS!!! The descripion of a todo cannot be empty.", exception.getMessage());
    }

    @Test
    public void parseTodoDescription_withExtraSpaces_trimsCorrectly() throws DukeException {
        String description = Parser.parseTodoDescription("todo   read book   ");
        assertEquals("read book", description);
    }

    // Test parseDeadlineCommand() method
    @Test
    public void parseDeadlineCommand_validInput_returnsDescriptionAndDeadline() throws DukeException {
        String[] parts = Parser.parseDeadlineCommand("deadline submit report /by 2025-08-30 18:00");
        assertEquals(2, parts.length);
        assertEquals("submit report", parts[0]);
        assertEquals("2025-08-30 18:00", parts[1]);
    }

    @Test
    public void parseDeadlineCommand_missingBy_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseDeadlineCommand("deadline submit report");
        });
        assertEquals("OOPS!!! Invalid deadline format.", exception.getMessage());
    }

    @Test
    public void parseDeadlineCommand_multipleBy_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseDeadlineCommand("deadline submit /by report /by 2025-08-30");
        });
        assertEquals("OOPS!!! Invalid deadline format.", exception.getMessage());
    }

    @Test
    public void parseDeadlineCommand_emptyDescription_throwsDukeException() {
        // When description is empty, there's no space before /by, so the split pattern
        // " /by " won't match
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseDeadlineCommand("deadline /by 2025-08-30 18:00");
        });
        assertEquals("OOPS!!! Invalid deadline format.", exception.getMessage());
    }

    // Test parseEventCommand() method
    @Test
    public void parseEventCommand_validInput_returnsDescriptionFromTo() throws DukeException {
        String[] parts = Parser.parseEventCommand("event meeting /from 2025-08-30 14:00 /to 2025-08-30 16:00");
        assertEquals(3, parts.length);
        assertEquals("meeting", parts[0]);
        assertEquals("2025-08-30 14:00", parts[1]);
        assertEquals("2025-08-30 16:00", parts[2]);
    }

    @Test
    public void parseEventCommand_missingFrom_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseEventCommand("event meeting /to 2025-08-30 16:00");
        });
        assertEquals("OOPS!!! Invalid event format.", exception.getMessage());
    }

    @Test
    public void parseEventCommand_missingTo_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseEventCommand("event meeting /from 2025-08-30 14:00");
        });
        assertEquals("OOPS!!! Invalid event format.", exception.getMessage());
    }

    @Test
    public void parseEventCommand_wrongOrder_stillParsesSuccessfully() throws DukeException {
        // The current regex pattern " /from | /to " splits on either pattern
        // So "event meeting /to 2025-08-30 16:00 /from 2025-08-30 14:00"
        // would actually parse successfully (though semantically wrong)
        String[] parts = Parser.parseEventCommand("event meeting /to 2025-08-30 16:00 /from 2025-08-30 14:00");
        assertEquals(3, parts.length);
        assertEquals("meeting", parts[0]);
        // The exact split depends on which pattern matches first
    }

    // Test parseDeleteIndex() method
    @Test
    public void parseDeleteIndex_validInput_returnsZeroBasedIndex() throws DukeException {
        int index = Parser.parseDeleteIndex("delete 3");
        assertEquals(2, index); // 3 - 1 = 2 (0-based)
    }

    @Test
    public void parseDeleteIndex_emptyIndex_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseDeleteIndex("delete");
        });
        assertEquals("OOPS!!! Please produced a task number.", exception.getMessage());
    }

    @Test
    public void parseDeleteIndex_whitespaceOnly_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseDeleteIndex("delete   ");
        });
        assertEquals("OOPS!!! Please produced a task number.", exception.getMessage());
    }

    @Test
    public void parseDeleteIndex_invalidNumber_throwsDukeException() {
        DukeException exception = assertThrows(DukeException.class, () -> {
            Parser.parseDeleteIndex("delete abc");
        });
        assertEquals("Invalid task number format.", exception.getMessage());
    }

    @Test
    public void parseDeleteIndex_negativeNumber_returnsNegativeIndex() throws DukeException {
        int index = Parser.parseDeleteIndex("delete -5");
        assertEquals(-6, index); // -5 - 1 = -6
    }
}
