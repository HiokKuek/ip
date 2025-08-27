package Wader;

import Wader.Util.DukeException;
import Wader.Task.Task;
import Wader.Util.Parser;
import Wader.Util.Storage;
import Wader.Util.Ui;
import Wader.Util.WaderList;
import Wader.Util.Parser.Command;
import Wader.Util.Parser.CommandType;

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
        // Echo user_input
        String user_input = "";

        while (true) {
            try {
                user_input = ui.readCommand();
                Parser.Command command = Parser.parse(user_input);

                if (command.getType() == Parser.CommandType.BYE) {
                    return; // Exit the loop
                } else if (command.getType() == Parser.CommandType.LIST) {
                    ui.showTaskList(tasks);
                } else if (command.getType() == Parser.CommandType.MARK) {
                    handleMark(command.getFullCommand(), tasks);
                } else if (command.getType() == Parser.CommandType.UNMARK) {
                    handleUnmark(command.getFullCommand(), tasks);
                } else if (command.getType() == Parser.CommandType.TODO) {
                    handleTodo(command.getFullCommand(), tasks);
                } else if (command.getType() == Parser.CommandType.DEADLINE) {
                    handleDeadline(command.getFullCommand(), tasks);
                } else if (command.getType() == Parser.CommandType.EVENT) {
                    handleEvent(command.getFullCommand(), tasks);
                } else if (command.getType() == Parser.CommandType.DELETE) {
                    handleDelete(command.getFullCommand(), tasks);
                } else {
                    throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }

                try {
                    storage.save(tasks);
                } catch (DukeException e) {
                    ui.showError("Error saving tasks: " + e.getMessage());
                }

            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("Invalid task number format.");
            }
        }
    }

    private void handleMark(String input, WaderList waderList) throws DukeException {
        int index = Parser.parseTaskIndex(input, "mark");
        boolean res = waderList.mark(index);
        if (res) {
            ui.showTaskMarked(waderList, index);
        } else {
            ui.showError("Invalid task index.");
        }
    }

    private void handleUnmark(String input, WaderList waderList) throws DukeException {
        int index = Parser.parseTaskIndex(input, "unmark");
        boolean res = waderList.unmark(index);
        if (res) {
            ui.showTaskUnmarked(waderList, index);
        } else {
            ui.showError("Invalid task index.");
        }
    }

    private void handleTodo(String input, WaderList waderList) throws DukeException {
        String desc = Parser.parseTodoDescription(input);
        Task task = waderList.addToDoTask(desc);
        ui.showTaskAdded(task, waderList);
    }

    private void handleDeadline(String input, WaderList waderList) throws DukeException {
        String[] parts = Parser.parseDeadlineCommand(input);
        Task task = waderList.addDeadlineTask(parts[0], parts[1]);
        ui.showTaskAdded(task, waderList);
    }

    private void handleEvent(String input, WaderList waderList) throws DukeException {
        String[] parts = Parser.parseEventCommand(input);
        Task task = waderList.addEventTask(parts[0], parts[1], parts[2]);
        ui.showTaskAdded(task, waderList);
    }

    private void handleDelete(String input, WaderList waderList) throws DukeException {
        try {
            int index = Parser.parseDeleteIndex(input);
            Task removedTask = waderList.delete(index);
            ui.showTaskDeleted(removedTask, waderList);
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Invalid task index");
        }
    }
}
