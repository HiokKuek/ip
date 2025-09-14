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
        assert filePath != null && !filePath.isEmpty() : "File path must not be null or empty";
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
        assert userInput != null && !userInput.isEmpty() : "User input must not be null or empty";
        try {
            Parser.Command command = Parser.parse(userInput);
            assert command != null : "Parsed command must not be null";

            switch (command.getType()) {
                case BYE:
                    storage.save(tasks);
                    return ui.showGoodbyeMessage();
                case LIST:
                    return ui.showTaskList(tasks);
                case MARK:
                    return handleMarkAndGetResponse(command.getFullCommand(), tasks);
                case UNMARK:
                    return handleUnmarkAndGetResponse(command.getFullCommand(), tasks);
                case TODO:
                    return handleTodoAndGetResponse(command.getFullCommand(), tasks);
                case DEADLINE:
                    return handleDeadlineAndGetResponse(command.getFullCommand(), tasks);
                case EVENT:
                    return handleEventAndGetResponse(command.getFullCommand(), tasks);
                case DELETE:
                    return handleDeleteAndGetResponse(command.getFullCommand(), tasks);
                case FIND:
                    return handleFindAndGetResponse(command.getFullCommand(), tasks);
                default:
                    throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
        } catch (DukeException e) {
            return ui.showError(e.getMessage());
        } catch (NumberFormatException e) {
            return ui.showError("Invalid task number format.");
        } catch (Exception e) {
            return ui.showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Wader("storage/tasks.txt").serve();
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
        assert index >= 0 : "Task index must be non-negative";
        boolean res = waderList.mark(index);
        if (res) {
            return ui.showTaskMarked(waderList, index);
        } else {
            return ui.showError("Invalid task index.");
        }
    }

    private String handleUnmarkAndGetResponse(String input, WaderList waderList) throws DukeException {
        int index = Parser.parseTaskIndex(input, "unmark");
        assert index >= 0 : "Task index must be non-negative";
        boolean res = waderList.unmark(index);
        if (res) {
            return ui.showTaskUnmarked(waderList, index);
        } else {
            return ui.showError("Invalid task index.");
        }
    }

    private String handleTodoAndGetResponse(String input, WaderList waderList) throws DukeException {
        String desc = Parser.parseTodoDescription(input);
        assert desc != null && !desc.isEmpty() : "Task description must not be null or empty";
        Task task = waderList.addToDoTask(desc);
        return ui.showTaskAdded(task, waderList);
    }

    private String handleDeadlineAndGetResponse(String input, WaderList waderList) throws DukeException {
        String[] parts = Parser.parseDeadlineCommand(input);
        assert parts.length == 2 : "Deadline command must have exactly two parts";
        Task task = waderList.addDeadlineTask(parts[0], parts[1]);
        return ui.showTaskAdded(task, waderList);
    }

    private String handleEventAndGetResponse(String input, WaderList waderList) throws DukeException {
        String[] parts = Parser.parseEventCommand(input);
        assert parts.length == 3 : "Event command must have exactly three parts";
        Task task = waderList.addEventTask(parts[0], parts[1], parts[2]);
        return ui.showTaskAdded(task, waderList);
    }

    private String handleDeleteAndGetResponse(String input, WaderList waderList) throws DukeException {
        int index = Parser.parseDeleteIndex(input);
        assert index >= 0 : "Task index must be non-negative";
        try {
            Task removedTask = waderList.delete(index);
            return ui.showTaskDeleted(removedTask, waderList);
        } catch (IndexOutOfBoundsException e) {
            return ui.showError("Invalid task index");
        }
    }

    private String handleFindAndGetResponse(String input, WaderList waderList) throws DukeException {
        String keyword = Parser.parseFindKeyword(input);
        assert keyword != null && !keyword.isEmpty() : "Find keyword must not be null or empty";
        List<Task> foundTasks = waderList.findTasks(keyword);
        return ui.showTaskList(foundTasks);
    }
}
