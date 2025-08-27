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
                if (user_input.startsWith("bye")) {
                    break;
                } else if (user_input.startsWith("list")) {
                    ui.showTaskList(tasks);
                } else if (user_input.startsWith("mark")) {
                    handleMark(user_input, tasks);
                } else if (user_input.startsWith("unmark")) {
                    handleUnmark(user_input, tasks);
                } else if (user_input.startsWith("todo")) {
                    handleTodo(user_input, tasks);
                } else if (user_input.startsWith("deadline")) {
                    handleDeadline(user_input, tasks);
                } else if (user_input.startsWith("event")) {
                    handleEvent(user_input, tasks);
                } else if (user_input.startsWith("delete")) {
                    handleDelete(user_input, tasks);
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
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new DukeException("Please provide a task number to mark.");
        }
        int index = Integer.parseInt(parts[1]) - 1;
        boolean res = waderList.mark(index);
        if (res) {
            ui.showTaskMarked(waderList, index);
        } else {
            ui.showError("Invalid task index.");
        }
    }

    private void handleUnmark(String input, WaderList waderList) throws DukeException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new DukeException("Please provide a task number to unmark.");
        }
        int index = Integer.parseInt(parts[1]) - 1;
        boolean res = waderList.unmark(index);
        if (res) {
            ui.showTaskUnmarked(waderList, index);
        } else {
            ui.showError("Invalid task index.");
        }
    }

    private void handleTodo(String input, WaderList waderList) throws DukeException {
        String desc = input.substring(4).strip(); // Remove "todo " prefix
        if (desc.isEmpty()) {
            throw new DukeException("OOPS!!! The descripion of a todo cannot be empty.");
        }
        Task task = waderList.addToDoTask(desc);
        ui.showTaskAdded(task, waderList);
    }

    private void handleDeadline(String input, WaderList waderList) throws DukeException {
        String content = input.substring(8).strip(); // Remove "deadline " prefix
        String[] parts = content.split(" /by ");
        if (parts.length != 2) {
            throw new DukeException("OOPS!!! Invalid deadline format.");
        }
        Task task = waderList.addDeadlineTask(parts[0], parts[1]);
        ui.showTaskAdded(task, waderList);
    }

    private void handleEvent(String input, WaderList waderList) throws DukeException {
        String content = input.substring(5).strip(); // Remove "event " prefix
        String[] parts = content.split(" /from | /to ");
        if (parts.length != 3) {
            throw new DukeException("OOPS!!! Invalid event format.");
        }
        Task task = waderList.addEventTask(parts[0], parts[1], parts[2]);
        ui.showTaskAdded(task, waderList);
    }

    private void handleDelete(String input, WaderList waderList) throws DukeException {
        String indexStr = input.substring(6).strip();
        if (indexStr.isEmpty()) {
            throw new DukeException("OOPS!!! Please produced a task number.");
        }
        try {
            int index = Integer.parseInt(indexStr) - 1;
            Task removedTask = waderList.delete(index);
            ui.showTaskDeleted(removedTask, waderList);
        } catch (IndexOutOfBoundsException e) {
            ui.showError("Invalid task index");
        }
    }
}
