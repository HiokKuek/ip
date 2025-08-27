import java.util.Scanner;
import java.io.FileWriter;

public class Wader {

    private WaderList tasks;

    public Wader(String filePath) {
        tasks = new WaderList();
    }

    public static void main(String[] args) {
        new Wader("Wader.txt").serve();
    }

    private void serve() {
        // Print Welcome Message
        System.out.println(Messages.getWelcomeMessage());

        // Take in user input
        scanInput();

        // Print Goodby Message
        System.out.println(Messages.getGoodbyeMessage());
    }

    private void scanInput() {
        // Echo user_input
        String user_input = "";
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            try {
                user_input = inputScanner.nextLine();
                user_input = user_input.strip();
                if (user_input.startsWith("bye")) {
                    break;
                } else if (user_input.startsWith("list")) {
                    printList(tasks);
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
                Wader.saveList(tasks);

            } catch (DukeException e) {
                System.out.println(Messages.printCustomMessage(e.getMessage()));
            } catch (NumberFormatException e) {
                System.out.println(Messages.printCustomMessage("Invalid task number format."));
            }
        }
        inputScanner.close();
    }

    private static void saveList(WaderList waderList) {
        try {
            String fileName = "Wader.txt";
            java.io.File file = new java.io.File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            String toWrite = "";
            for (int i = 0; i < waderList.getSize(); i++) {
                toWrite += waderList.getTaskString(i) + "\n";
            }
            writer.write(toWrite);
            writer.close();
        } catch (java.io.IOException e) {
            System.out.println("An error occurred while saving the file: " + e.getMessage());
        }
    }

    private static void printList(WaderList waderList) {
        if (waderList.isEmpty()) {
            System.out.println(Messages.printCustomMessage("No tasks in the list."));
            return;
        }
        String prnt = waderList.getTasks()
                .stream()
                .map(t -> String.format("%d.%s", waderList.getTasks().indexOf(t) + 1, t))
                .reduce((x, y) -> x + "\n" + y)
                .get();
        String message = Messages.printCustomMessage(prnt);
        System.out.print(message);
    }

    private static void handleMark(String input, WaderList waderList) throws DukeException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new DukeException("Please provide a task number to mark.");
        }
        int index = Integer.parseInt(parts[1]) - 1;
        boolean res = waderList.mark(index);
        if (res) {
            String prntMessage = "Nice! I've marked this task as done:\n" + Messages.INDENTATION +
                    waderList.getTaskString(index);
            System.out.println(Messages.printCustomMessage(prntMessage));
        } else {
            System.out.println(Messages.printCustomMessage("Invalid task index."));
        }
    }

    private static void handleUnmark(String input, WaderList waderList) throws DukeException {
        String[] parts = input.split(" ");
        if (parts.length < 2) {
            throw new DukeException("Please provide a task number to unmark.");
        }
        int index = Integer.parseInt(parts[1]) - 1;
        boolean res = waderList.unmark(index);
        if (res) {
            String prntMessage = "OK, I've marked this task as not done yet:\n" + Messages.INDENTATION +
                    waderList.getTaskString(index);
            System.out.println(Messages.printCustomMessage(prntMessage));
        } else {
            System.out.println(Messages.printCustomMessage("Invalid task index."));
        }
    }

    private static void handleTodo(String input, WaderList waderList) throws DukeException {
        String desc = input.substring(4).strip(); // Remove "todo " prefix
        if (desc.isEmpty()) {
            throw new DukeException("OOPS!!! The descripion of a todo cannot be empty.");
        }
        Task task = waderList.addToDoTask(desc);
        printTaskAdded(task, waderList);
    }

    private static void handleDeadline(String input, WaderList waderList) throws DukeException {
        String content = input.substring(8).strip(); // Remove "deadline " prefix
        String[] parts = content.split(" /by ");
        if (parts.length != 2) {
            throw new DukeException("OOPS!!! Invalid deadline format.");
        }
        Task task = waderList.addDeadlineTask(parts[0], parts[1]);
        printTaskAdded(task, waderList);
    }

    private static void handleEvent(String input, WaderList waderList) throws DukeException {
        String content = input.substring(5).strip(); // Remove "event " prefix
        String[] parts = content.split(" /from | /to ");
        if (parts.length != 3) {
            throw new DukeException("OOPS!!! Invalid event format.");
        }
        Task task = waderList.addEventTask(parts[0], parts[1], parts[2]);
        printTaskAdded(task, waderList);
    }

    private static void handleDelete(String input, WaderList waderList) throws DukeException {
        String indexStr = input.substring(6).strip();
        if (indexStr.isEmpty()) {
            throw new DukeException("OOPS!!! Please produced a task number.");
        }
        try {
            int index = Integer.parseInt(indexStr) - 1;
            Task removedTask = waderList.delete(index);
            String message = "Noted. I've removed this task:\n" + Messages.INDENTATION +
                    removedTask.toString() + "\n" +
                    "Now you have " + waderList.getSize() + " tasks in the list.";
            System.out.println(Messages.printCustomMessage(message));
        } catch (IndexOutOfBoundsException e) {
            System.out.println(Messages.printCustomMessage("Invalid task index"));
        }
    }

    private static void printTaskAdded(Task task, WaderList waderList) {
        String message = "Got it. I've added this task:\n" + Messages.INDENTATION +
                task.toString() + "\n" +
                "Now you have " + waderList.getSize() + " tasks in the list.";
        System.out.println(Messages.printCustomMessage(message));
    }
}
