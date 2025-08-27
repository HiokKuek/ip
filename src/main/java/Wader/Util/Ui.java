package Wader.util;

import java.util.Scanner;

import Wader.util.Messages;

import Wader.Task.Task;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcomeMessage() {
        System.out.println(Messages.getWelcomeMessage());
    }

    public void showGoodbyeMessage() {
        System.out.println(Messages.getGoodbyeMessage());
    }

    public String readCommand() {
        return scanner.nextLine().strip();
    }

    public void close() {
        scanner.close();
    }

    public void showError(String errorMessage) {
        System.out.println(Messages.printCustomMessage(errorMessage));
    }

    public void showMessage(String message) {
        System.out.println(Messages.printCustomMessage(message));
    }

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

    public void showTaskAdded(Task task, WaderList waderList) {
        String message = "Got it. I've added this task:\n" + Messages.INDENTATION +
                task.toString() + "\n" +
                "Now you have " + waderList.getSize() + " tasks in the list.";
        showMessage(message);
    }

    public void showTaskMarked(WaderList waderList, int index) {
        String prntMessage = "Nice! I've marked this task as done:\n" + Messages.INDENTATION +
                waderList.getTaskString(index);
        showMessage(prntMessage);
    }

    public void showTaskUnmarked(WaderList waderList, int index) {
        String prntMessage = "OK, I've marked this task as not done yet:\n" + Messages.INDENTATION +
                waderList.getTaskString(index);
        showMessage(prntMessage);
    }

    public void showTaskDeleted(Task removedTask, WaderList waderList) {
        String message = "Noted. I've removed this task:\n" + Messages.INDENTATION +
                removedTask.toString() + "\n" +
                "Now you have " + waderList.getSize() + " tasks in the list.";
        showMessage(message);
    }
}
