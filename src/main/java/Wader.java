import java.util.Scanner;

public class Wader {

    WaderList waderList;

    public Wader() {
        this.waderList = new WaderList();
    }

    public static void main(String[] args) {
        Wader.serve();
    }

    private static void serve() {
        // Print Welcome Message
        System.out.println(Messages.getWelcomeMessage());

        // Take in user input
        scanInput();

        // Print Goodby Message
        System.out.println(Messages.getGoodbyeMessage());
    }

    private static void scanInput() {
        // Echo user_input
        String user_input = "";
        Scanner inputScanner = new Scanner(System.in);
        WaderList myList = new WaderList();

        while (true) {
            try {
                user_input = inputScanner.nextLine();
                user_input = user_input.strip();
                if (user_input.startsWith("bye")) {
                    break;
                } else if (user_input.startsWith("list")) {
                    myList.printList();
                } else if (user_input.startsWith("mark")) {
                    int index = Integer.parseInt(user_input.split(" ")[1]) - 1;
                    boolean res = myList.mark(index);
                    if (res) {
                        String prntMessage = "Nice! I've marked this task as done:\n" + Messages.INDENTATION +
                                myList.getTaskString(index);
                        System.out.println(Messages.printCustomMessage(prntMessage));
                    } else {
                        System.out.println(Messages.printCustomMessage("Invalid task index."));
                    }

                } else if (user_input.startsWith("unmark")) {
                    int index = Integer.parseInt(user_input.split(" ")[1]) - 1;
                    boolean res = myList.unmark(index);
                    if (res) {
                        String prntMessage = "OK, I've marked this task as not done yet:\n" + Messages.INDENTATION +
                                myList.getTaskString(index);
                        System.out.println(Messages.printCustomMessage(prntMessage));
                    } else {
                        System.out.println(Messages.printCustomMessage("Invalid task index."));
                    }
                } else {
                    myList.addAndPrint(user_input);
                }

            } catch (DukeException e) {
                System.out.println(Messages.printCustomMessage(e.getMessage()));
            }
        }
        inputScanner.close();

    }
}
