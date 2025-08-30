package Wader.Util;

import Wader.Util.DukeException;

public class Parser {

    public enum CommandType {
        BYE, LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, FIND, UNKNOWN
    }

    public static class Command {
        private CommandType type;
        private String fullCommand;

        public Command(CommandType type, String fullCommand) {
            this.type = type;
            this.fullCommand = fullCommand;
        }

        public CommandType getType() {
            return type;
        }

        public String getFullCommand() {
            return fullCommand;
        }
    }

    public static Command parse(String userInput) {
        String trimmedInput = userInput.strip();

        if (trimmedInput.startsWith("bye")) {
            return new Command(CommandType.BYE, trimmedInput);
        } else if (trimmedInput.startsWith("list")) {
            return new Command(CommandType.LIST, trimmedInput);
        } else if (trimmedInput.startsWith("mark")) {
            return new Command(CommandType.MARK, trimmedInput);
        } else if (trimmedInput.startsWith("unmark")) {
            return new Command(CommandType.UNMARK, trimmedInput);
        } else if (trimmedInput.startsWith("todo")) {
            return new Command(CommandType.TODO, trimmedInput);
        } else if (trimmedInput.startsWith("deadline")) {
            return new Command(CommandType.DEADLINE, trimmedInput);
        } else if (trimmedInput.startsWith("event")) {
            return new Command(CommandType.EVENT, trimmedInput);
        } else if (trimmedInput.startsWith("delete")) {
            return new Command(CommandType.DELETE, trimmedInput);

        } else if (trimmedInput.startsWith("find")) {
            return new Command(CommandType.FIND, trimmedInput);
        } else {
            return new Command(CommandType.UNKNOWN, trimmedInput);
        }
    }

    public static String parseFindKeyword(String input) throws DukeException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new DukeException("Please provide a keyword for find.");
        }
        return parts[1];
    }

    public static int parseTaskIndex(String input, String commandPrefix) throws DukeException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new DukeException("Please provide a task number for " + commandPrefix + ".");
        }
        try {
            return Integer.parseInt(parts[1]) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DukeException("Invalid task number format.");
        }
    }

    public static String parseTodoDescription(String input) throws DukeException {
        String desc = input.substring(4).strip(); // Remove "todo " prefix
        if (desc.isEmpty()) {
            throw new DukeException("OOPS!!! The descripion of a todo cannot be empty.");
        }
        return desc;
    }

    public static String[] parseDeadlineCommand(String input) throws DukeException {
        String content = input.substring(8).strip(); // Remove "deadline " prefix
        String[] parts = content.split(" /by ");
        if (parts.length != 2) {
            throw new DukeException("OOPS!!! Invalid deadline format.");
        }
        return parts; // [description, deadline]
    }

    public static String[] parseEventCommand(String input) throws DukeException {
        String content = input.substring(5).strip(); // Remove "event " prefix
        String[] parts = content.split(" /from | /to ");
        if (parts.length != 3) {
            throw new DukeException("OOPS!!! Invalid event format.");
        }
        return parts; // [description, from, to]
    }

    public static int parseDeleteIndex(String input) throws DukeException {
        String indexStr = input.substring(6).strip();
        if (indexStr.isEmpty()) {
            throw new DukeException("OOPS!!! Please produced a task number.");
        }
        try {
            return Integer.parseInt(indexStr) - 1; // Convert to 0-based index
        } catch (NumberFormatException e) {
            throw new DukeException("Invalid task number format.");
        }
    }
}
