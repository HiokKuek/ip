import java.util.Scanner;

public class Wader {
    public static void main(String[] args) {
        Wader.printMessages();
    }

    private static void printMessages() {
        // Print Welcome Message
        System.out.println(Messages.getWelcomeMessage());

        // Echo user_input
        String user_input = "";
        Scanner inputScanner = new Scanner(System.in);
        MyList myList = new MyList();
        while (true) {
            user_input = inputScanner.nextLine();
            user_input = user_input.strip();
            if (user_input.startsWith("bye")) {
                break;
            } else if (user_input.startsWith("list")) {
                myList.printList();
            } else if (user_input.startsWith("mark")) {
                int index = Integer.parseInt(user_input.split(" ")[1]) - 1;
                myList.markAndPrint(index, 1);

            } else if (user_input.startsWith("unmark")) {
                int index = Integer.parseInt(user_input.split(" ")[1]) - 1;
                myList.markAndPrint(index, 0);
            } else {
                myList.addAndPrint(user_input);
            }
        }
        inputScanner.close();

        // Print Goodby Message
        System.out.println(Messages.getGoodbyeMessage());

    }
}
