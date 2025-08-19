public class Wader {
    public static void main(String[] args) {
        String horizontal_line = "____________________________________________________________\n";
        String welcome_message = "Hello! I'm Wader\n" +
                "What can I do for you?\n";
        String goodbye_message = "Bye. Hope to see you soon!\n";

        String message = horizontal_line +
                welcome_message +
                horizontal_line +
                goodbye_message +
                horizontal_line;

        System.out.println(message);
    }
}
