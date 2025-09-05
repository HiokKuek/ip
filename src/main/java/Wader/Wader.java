package wader;

import java.util.List;

import wader.task.Task;
import wader.util.DukeException;
import wader.util.Parser;
import wader.util.Storage;
import wader.util.Ui;
import wader.util.WaderList;

public class Wader {

    private WaderList tasks;
    private Ui ui;
    private Storage storage;

    public Wader(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = storage.load();
        } catch (DukeException e) {
            ui.showError(e.getMessage());
            tasks = new WaderList();
        }
    }

    /**
     * Handles user input and returns the appropriate response.
     * This method processes the input command and returns the response string
     * that would be displayed to the user.
     *
     * @param userInput the user's input command
     * @return the response message from processing the command
     */
    public String getResponse(String userInput) {
        try {
            Parser.Command command = Parser.parse(userInput);

            if (command.getType() == Parser.CommandType.BYE) {
                return ui.showGoodbyeMessage();
            } else if (command.getType() == Parser.CommandType.LIST) {
                return ui.showTaskList(tasks);
            } else if (command.getType() == Parser.CommandType.MARK) {
                return handleMarkAndGetResponse(command.getFullCommand(), tasks);
            } else if (command.getType() == Parser.CommandType.UNMARK) {
                return handleUnmarkAndGetResponse(command.getFullCommand(), tasks);
            } else if (command.getType() == Parser.CommandType.TODO) {
                return handleTodoAndGetResponse(command.getFullCommand(), tasks);
            } else if (command.getType() == Parser.CommandType.DEADLINE) {
                return handleDeadlineAndGetResponse(command.getFullCommand(), tasks);
            } else if (command.getType() == Parser.CommandType.EVENT) {
                return handleEventAndGetResponse(command.getFullCommand(), tasks);
            } else if (command.getType() == Parser.CommandType.DELETE) {
                return handleDeleteAndGetResponse(command.getFullCommand(), tasks);
            } else if (command.getType() == Parser.CommandType.FIND) {
                return handleFindAndGetResponse(command.getFullCommand(), tasks);
            } else {
                throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
        } catch (DukeException e) {
            return ui.showError(e.getMessage());
        } catch (NumberFormatException e) {
            return ui.showError("Invalid task number format.");
        } catch (Exception e) {
            return ui.showError("An unexpected error occurred: " + e.getMessage());
        } finally {
            // Save tasks after each command (except for bye)
            if (!userInput.trim().startsWith("bye")) {
                try {
                    storage.save(tasks);
                } catch (DukeException e) {
                    // Note: This error message won't be returned since we're in finally block
                    // But it will still be displayed via ui.showError
                    ui.showError("Error saving tasks: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        new Wader("Wader.txt").serve();
    }

    private void serve() {
        // Print Welcome Message
        ui.showWelcomeMessage();

        // Take in user input
        scanInput();

        // Print Goodbye Message
        ui.showGoodbyeMessage();
        ui.close();
    }

    private void scanInput() {
        // Echo userInput
        String userInput = "";

        while (true) {
            userInput = ui.readCommand();

            // Use handleInput to process the command
            getResponse(userInput);

            // Check if it's a bye command to exit
            if (userInput.trim().startsWith("bye")) {
                break;
            }
        }
    }

    // New methods that return response strings for handleInput()
    private String handleMarkAndGetResponse(String input, WaderList waderList) throws DukeException {
        int index = Parser.parseTaskIndex(input, "mark");
        boolean res = waderList.mark(index);
        if (res) {
            return ui.showTaskMarked(waderList, index);
        } else {
            return ui.showError("Invalid task index.");
        }
    }

    private String handleUnmarkAndGetResponse(String input, WaderList waderList) throws DukeException {
        int index = Parser.parseTaskIndex(input, "unmark");
        boolean res = waderList.unmark(index);
        if (res) {
            return ui.showTaskUnmarked(waderList, index);
        } else {
            return ui.showError("Invalid task index.");
        }
    }

    private String handleTodoAndGetResponse(String input, WaderList waderList) throws DukeException {
        String desc = Parser.parseTodoDescription(input);
        Task task = waderList.addToDoTask(desc);
        return ui.showTaskAdded(task, waderList);
    }

    private String handleDeadlineAndGetResponse(String input, WaderList waderList) throws DukeException {
        String[] parts = Parser.parseDeadlineCommand(input);
        Task task = waderList.addDeadlineTask(parts[0], parts[1]);
        return ui.showTaskAdded(task, waderList);
    }

    private String handleEventAndGetResponse(String input, WaderList waderList) throws DukeException {
        String[] parts = Parser.parseEventCommand(input);
        Task task = waderList.addEventTask(parts[0], parts[1], parts[2]);
        return ui.showTaskAdded(task, waderList);
    }

    private String handleDeleteAndGetResponse(String input, WaderList waderList) throws DukeException {
        try {
            int index = Parser.parseDeleteIndex(input);
            Task removedTask = waderList.delete(index);
            return ui.showTaskDeleted(removedTask, waderList);
        } catch (IndexOutOfBoundsException e) {
            return ui.showError("Invalid task index");
        }
    }

    private String handleFindAndGetResponse(String input, WaderList waderList) throws DukeException {
        String keyword = Parser.parseFindKeyword(input);
        List<Task> foundTasks = waderList.findTasks(keyword);
        return ui.showTaskList(foundTasks);
    }
}
