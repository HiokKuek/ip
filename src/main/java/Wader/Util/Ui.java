package Wader.Util;

import java.util.Scanner;
import java.util.List;

import Wader.Task.Task;

/**
 * Handles all user interface interactions for the Wader application.
 * This class is responsible for displaying messages to the user, reading user
 * input,
 * and formatting output for various operations like task management and error
 * handling.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Constructs a new Ui object and initializes the Scanner for reading user
     * input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message when the application starts.
     * Uses the Messages utility class to get the formatted welcome message.
     */
    public void showWelcomeMessage() {
        System.out.println(Messages.getWelcomeMessage());
    }

    /**
     * Displays the goodbye message when the application terminates.
     * Uses the Messages utility class to get the formatted goodbye message.
     */
    public void showGoodbyeMessage() {
        System.out.println(Messages.getGoodbyeMessage());
    }

    /**
     * Reads a command from the user through standard input.
     * The input is automatically trimmed of leading and trailing whitespace.
     * 
     * @return the user's input as a trimmed string
     */
    public String readCommand() {
        return scanner.nextLine().strip();
    }

    /**
     * Closes the Scanner to release system resources.
     * Should be called when the application terminates to prevent resource leaks.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays an error message to the user.
     * The error message is formatted using the Messages utility class for
     * consistent styling.
     * 
     * @param errorMessage the error message to display to the user
     */
    public void showError(String errorMessage) {
        System.out.println(Messages.printCustomMessage(errorMessage));
    }

    /**
     * Displays a general message to the user.
     * The message is formatted using the Messages utility class for consistent
     * styling.
     * 
     * @param message the message to display to the user
     */
    public void showMessage(String message) {
        System.out.println(Messages.printCustomMessage(message));
    }

    /**
     * Displays the current list of tasks to the user.
     * Each task is numbered starting from 1 and displayed with its current status.
     * If the task list is empty, displays a message indicating no tasks are
     * present.
     * 
     * @param waderList the WaderList containing tasks to be displayed
     */
    public void showTaskList(WaderList waderList) {
        if (waderList.isEmpty()) {
            showMessage("No tasks in the list.");
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

    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            showMessage("No matching tasks found.");
            return;
        }
        String prnt = tasks
                .stream()
                .map(t -> String.format("%d.%s", tasks.indexOf(t) + 1, t))
                .reduce((x, y) -> x + "\n" + y)
                .get();
        String message = Messages.printCustomMessage(prnt);
        System.out.print(message);
    }

    /**
     * Displays a confirmation message when a task has been successfully added.
     * Shows the added task details and the updated total number of tasks in the
     * list.
     * 
     * @param task      the Task object that was added to the list
     * @param waderList the WaderList containing all tasks (used to get the current
     *                  size)
     */
    public void showTaskAdded(Task task, WaderList waderList) {
        String message = "Got it. I've added this task:\n" + Messages.INDENTATION +
                task.toString() + "\n" +
                "Now you have " + waderList.getSize() + " tasks in the list.";
        showMessage(message);
    }

    /**
     * Displays a confirmation message when a task has been marked as done.
     * Shows the task that was marked with its updated completion status.
     * 
     * @param waderList the WaderList containing the task that was marked
     * @param index     the index of the task that was marked as done (0-based)
     */
    public void showTaskMarked(WaderList waderList, int index) {
        String prntMessage = "Nice! I've marked this task as done:\n" + Messages.INDENTATION +
                waderList.getTaskString(index);
        showMessage(prntMessage);
    }

    /**
     * Displays a confirmation message when a task has been unmarked (marked as not
     * done).
     * Shows the task that was unmarked with its updated completion status.
     * 
     * @param waderList the WaderList containing the task that was unmarked
     * @param index     the index of the task that was marked as not done (0-based)
     */
    public void showTaskUnmarked(WaderList waderList, int index) {
        String prntMessage = "OK, I've marked this task as not done yet:\n" + Messages.INDENTATION +
                waderList.getTaskString(index);
        showMessage(prntMessage);
    }

    /**
     * Displays a confirmation message when a task has been successfully deleted.
     * Shows the deleted task details and the updated total number of tasks
     * remaining in the list.
     * 
     * @param removedTask the Task object that was removed from the list
     * @param waderList   the WaderList after the task was removed (used to get the
     *                    current size)
     */
    public void showTaskDeleted(Task removedTask, WaderList waderList) {
        String message = "Noted. I've removed this task:\n" + Messages.INDENTATION +
                removedTask.toString() + "\n" +
                "Now you have " + waderList.getSize() + " tasks in the list.";
        showMessage(message);
    }
}
