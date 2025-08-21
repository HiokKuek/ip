import java.util.ArrayList;
import java.util.List;

public class MyList {
    List<Task> items;

    public MyList() {
        items = new ArrayList<>();
    }

    public void add(String item) {
        Task newTask = new Task(item);
        items.add(newTask);
    }

    private void markIndexAsDone(int index) {
        items.get(index).markAsDone();
    }

    private void markIndexAsNotDone(int index) {
        items.get(index).markAsNotDone();
    }

    public void markAndPrint(int index, int action) {
        try {
            String prntMessage;
            if (action == 1) {
                markIndexAsDone(index);
                prntMessage = "Nice! I've marked this task as done:\n";
            } else {
                markIndexAsNotDone(index);
                prntMessage = "OK, I've marked this task as not done yet:\n";
            }
            prntMessage += items.get(index).toString();
            System.out.println(Messages.printCustomMessage(prntMessage));
        } catch (IndexOutOfBoundsException e) {
            String prntMessage = Messages.printCustomMessage("Invalid task index");
            System.out.println(prntMessage);
        }
    }

    public void printList() {
        String prnt = items
                .stream()
                .map(t -> String.format("%d.%s", items.indexOf(t) + 1, t))
                .reduce((x, y) -> x + "\n" + y)
                .get();
        String message = Messages.printCustomMessage(prnt);
        System.out.print(message);
    }

}