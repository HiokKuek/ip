package wader.util;

import java.util.List;
import java.util.Scanner;

import wader.task.Task;

/**
 * Handles all user interface interactions for the Wader application. This class
 * is responsible for
 * displaying messages to the user, reading user input, and formatting output
 * for various operations
 * like task management and error handling.
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
     * Displays the welcome message when the application starts. Uses the Messages
     * utility class to
     * get the formatted welcome message.
     *
     * @return the welcome message that was displayed
     */
    public String showWelcomeMessage() {
        String message = Messages.getWelcomeMessage();
        System.out.println(message);
        return message;
    }

    /**
     * Displays the goodbye message when the application terminates. Uses the
     * Messages utility class
     * to get the formatted goodbye message.
     *
     * @return the goodbye message that was displayed
     */
    public String showGoodbyeMessage() {
        String message = Messages.getGoodbyeMessage();
        System.out.println(message);
        return message;
    }

    /**
     * Reads a command from the user through standard input. The input is
     * automatically trimmed of
     * leading and trailing whitespace.
     *
     * @return the user's input as a trimmed string
     */
    public String readCommand() {
        return scanner.nextLine().strip();
    }

    /**
     * Closes the Scanner to release system resources. Should be called when the
     * application
     * terminates to prevent resource leaks.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays an error message to the user. The error message is formatted using
     * the Messages
     * utility class for consistent styling.
     *
     * @param errorMessage the error message to display to the user
     * @return the formatted error message that was displayed
     */
    public String showError(String errorMessage) {
        String formattedMessage = Messages.printCustomMessage(errorMessage);
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Displays a general message to the user. The message is formatted using the
     * Messages utility
     * class for consistent styling.
     *
     * @param message the message to display to the user
     * @return the formatted message that was displayed
     */
    public String showMessage(String message) {
        String formattedMessage = Messages.printCustomMessage(message);
        System.out.println(formattedMessage);
        return formattedMessage;
    }

    /**
     * Displays the current list of tasks to the user. Each task is numbered
     * starting from 1 and
     * displayed with its current status. If the task list is empty, displays a
     * message indicating
     * no tasks are present.
     *
     * @param waderList the WaderList containing tasks to be displayed
     * @return the formatted task list message that was displayed
     */
    public String showTaskList(WaderList waderList) {
        if (waderList.isEmpty()) {
            return showMessage("No tasks in the list.");
        }
        String prnt = waderList.getTasks().stream()
                .map(t -> String.format("%d.%s", waderList.getTasks().indexOf(t) + 1, t))
                .reduce((x, y) -> x + "\n" + y).get();
        String message = Messages.printCustomMessage(prnt);
        System.out.print(message);
        return message;
    }

    /**
     * Displays the current list of tasks to the user. Each task is numbered
     * starting from 1 and
     * displayed with its current status. If the task list is empty, displays a
     * message indicating
     * no matching tasks were found.
     *
     * @param tasks the list of tasks to be displayed
     * @return the formatted task list message that was displayed
     */
    public String showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return showMessage("No matching tasks found.");
        }
        String prnt = tasks.stream().map(t -> String.format("%d.%s", tasks.indexOf(t) + 1, t))
                .reduce((x, y) -> x + "\n" + y).get();
        String message = Messages.printCustomMessage(prnt);
        System.out.print(message);
        return message;
    }

    /**
     * Displays a confirmation message when a task has been successfully added.
     * Shows the added task
     * details and the updated total number of tasks in the list.
     *
     * @param task      the Task object that was added to the list
     * @param waderList the WaderList containing all tasks (used to get the current
     *                  size)
     * @return the formatted confirmation message that was displayed
     */
    public String showTaskAdded(Task task, WaderList waderList) {
        String message = "Got it. I've added this task:\n" + Messages.INDENTATION + task.toString()
                + "\n" + "Now you have " + waderList.getSize() + " tasks in the list.";
        return showMessage(message);
    }

    /**
     * Displays a confirmation message when a task has been marked as done. Shows
     * the task that was
     * marked with its updated completion status.
     *
     * @param waderList the WaderList containing the task that was marked
     * @param index     the index of the task that was marked as done (0-based)
     * @return the formatted confirmation message that was displayed
     */
    public String showTaskMarked(WaderList waderList, int index) {
        String prntMessage = "Nice! I've marked this task as done:\n" + Messages.INDENTATION
                + waderList.getTaskString(index);
        return showMessage(prntMessage);
    }

    /**
     * Displays a confirmation message when a task has been unmarked (marked as not
     * done). Shows the
     * task that was unmarked with its updated completion status.
     *
     * @param waderList the WaderList containing the task that was unmarked
     * @param index     the index of the task that was marked as not done (0-based)
     * @return the formatted confirmation message that was displayed
     */
    public String showTaskUnmarked(WaderList waderList, int index) {
        String prntMessage = "OK, I've marked this task as not done yet:\n" + Messages.INDENTATION
                + waderList.getTaskString(index);
        return showMessage(prntMessage);
    }

    /**
     * Displays a confirmation message when a task has been successfully deleted.
     * Shows the deleted
     * task details and the updated total number of tasks remaining in the list.
     *
     * @param removedTask the Task object that was removed from the list
     * @param waderList   the WaderList after the task was removed (used to get the
     *                    current size)
     * @return the formatted confirmation message that was displayed
     */
    public String showTaskDeleted(Task removedTask, WaderList waderList) {
        String message = "Noted. I've removed this task:\n" + Messages.INDENTATION + removedTask.toString()
                + "\n" + "Now you have " + waderList.getSize() + " tasks in the list.";
        return showMessage(message);
    }

    /**
     * Displays the next upcoming tasks to the user.
     * Shows up to the requested number of tasks that have dates and are upcoming.
     *
     * @param tasks the list of upcoming tasks to display
     * @return the formatted message showing the upcoming tasks
     */
    public String showNextUpcomingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return showMessage("There are no upcoming tasks.");
        }

        StringBuilder sb = new StringBuilder("Here are your next upcoming tasks:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(Messages.INDENTATION).append(String.format("%d. %s", i + 1, tasks.get(i).toString()));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return showMessage(sb.toString());
    }
}
