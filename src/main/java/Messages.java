public class Messages {
    public static final String HORIZONTAL_LINE = "____________________________________________________________\n";
    public static final String WELCOME_MESSAGE = "Hello! I'm Wader\n" +
            "What can I do for you?\n";
    public static final String GOODBYE_MESSAGE = "Bye. Hope to see you soon!\n";
    public static final String INDENTATION = "\u00A0\u00A0";

    public static String getWelcomeMessage() {
        return HORIZONTAL_LINE + WELCOME_MESSAGE + HORIZONTAL_LINE;
    }

    public static String getGoodbyeMessage() {
        return HORIZONTAL_LINE + GOODBYE_MESSAGE + HORIZONTAL_LINE;
    }

    public static String printCustomMessage(String message) {
        return HORIZONTAL_LINE + message + "\n" + HORIZONTAL_LINE;
    }

}
