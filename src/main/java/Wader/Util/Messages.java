package Wader.Util;

public class Messages {
    public static final String HORIZONTAL_LINE = "____________________________________________________________\n\n";
    public static final String WELCOME_MESSAGE = "Hello! I'm Wader\n" +
            "What can I do for you?\n";
    public static final String GOODBYE_MESSAGE = "Bye. Hope to see you soon!\n";
    public static final String INDENTATION = "\u00A0\u00A0";

    /**
     * Gets the welcome message for the user.
     * 
     * @return The welcome message.
     */
    public static String getWelcomeMessage() {
        return HORIZONTAL_LINE + WELCOME_MESSAGE + HORIZONTAL_LINE;
    }

    /**
     * Gets the goodbye message for the user.
     * 
     * @return The goodbye message.
     */
    public static String getGoodbyeMessage() {
        return HORIZONTAL_LINE + GOODBYE_MESSAGE + HORIZONTAL_LINE;
    }

    /**
     * Prints a custom message for the user by adding horizontal lines above and
     * below the input message.
     * 
     * @param message The custom message to print.
     * @return The formatted custom message.
     */
    public static String printCustomMessage(String message) {
        return HORIZONTAL_LINE + message + "\n" + HORIZONTAL_LINE;
    }

}
