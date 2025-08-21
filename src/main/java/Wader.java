import java.util.Scanner;

public class Wader {
    public static void main(String[] args) {
        String horizontal_line = "____________________________________________________________\n";
        String welcome_message = "Hello! I'm Wader\n" +
                "What can I do for you?\n";
        String goodbye_message = "Bye. Hope to see you soon!\n";

        String wel_message = horizontal_line +
                welcome_message +
                horizontal_line;
        String bybye_message = goodbye_message + horizontal_line;
        Scanner inputScanner = new Scanner(System.in);
        System.out.println(wel_message);

        String user_input = "";
        while (!user_input.equals("bye")) {
            user_input = inputScanner.nextLine();
            System.out.println(horizontal_line);
            System.out.println("Added: " + user_input);
            System.out.println(horizontal_line);
        }
        System.out.println(bybye_message);

    }
}
