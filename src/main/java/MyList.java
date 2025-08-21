import java.util.ArrayList;
import java.util.List;

public class MyList {
    List<String> items;

    public MyList() {
        items = new ArrayList<>();
    }

    public void add(String item) {
        items.add(item);
    }

    public void printList() {
        String prnt = items
                .stream()
                .reduce("", (x, y) -> x == "" ? String.format("%d. %s", items.indexOf(y) + 1, y)
                        : x + String.format("\n%d. %s", items.indexOf(y) + 1, y));
        String message = Messages.printCustomMessage(prnt);
        System.out.print(message);
    }

}