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