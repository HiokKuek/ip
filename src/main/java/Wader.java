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
        while (!user_input.equals("bye")) {
            user_input = inputScanner.nextLine();
            switch (user_input) {
                case "list":
                    myList.printList();
                    break;

                default:
                    myList.add(user_input);
                    System.out.println(Messages.printCustomMessage("Added: " + user_input));
                    break;

            }
        }
        inputScanner.close();

        // Print Goodby Message
        System.out.println(Messages.getGoodbyeMessage());

    }
}
