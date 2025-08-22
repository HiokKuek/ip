import java.util.ArrayList;
import java.util.List;

public class MyList {
    List<Task> items;

    public MyList() {
        items = new ArrayList<>();
    }

    public void addAndPrint(String item) throws DukeException {
        Task task;
        if (item.startsWith("todo")) {
            item = item.substring(4).strip(); // Remove "todo " prefix
            if (item == "")
                throw new DukeException("OOPS!!! The descripion of a todo cannot be empty.");
            task = new ToDoTask(item);
            addAndPrint(task);

        } else if (item.startsWith("deadline")) {
            item = item.substring(8).strip(); // Remove "deadline " prefix
            String[] parts = item.split(" /by ");
            if (parts.length != 2)
                throw new DukeException("OOPS!!! Invalid deadline format.");
            task = new DeadlineTask(parts[0], parts[1]);
            addAndPrint(task);

        } else if (item.startsWith("event")) {
            item = item.substring(5).strip(); // Remove "event " prefix
            String[] parts = item.split(" /from | /to ");
            if (parts.length != 3)
                throw new DukeException("OOPS!!! Invalid event format.");
            task = new EventTask(parts[0], parts[1], parts[2]);
            addAndPrint(task);
        } else if (item.startsWith("delete")) {
            item = item.substring(6).strip();
            if (item == "") {
                throw new DukeException("OOPS!!! Please produced a task number.");
            }
            int index = Integer.parseInt(item) - 1;
            deleteAndPrint(index);

        } else {
            // Default case, treat as a generic task
            throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    private void deleteAndPrint(int index) {
        try {
            Task removedTask = items.remove(index);
            String message = "Noted. I've removed this task:\n" + Messages.INDENTATION +
                    removedTask.toString() + "\n" +
                    "Now you have " + items.size() + " tasks in the list.";
            System.out.println(Messages.printCustomMessage(message));
        } catch (IndexOutOfBoundsException e) {
            System.out.println(Messages.printCustomMessage("Invalid task index"));
        }
    }

    private void addAndPrint(Task task) {
        items.add(task);
        String message = "Got it. I've added this task:\n" + Messages.INDENTATION +
                task.toString() + "\n" +
                "Now you have " + items.size() + " tasks in the list.";
        System.out.println(Messages.printCustomMessage(message));
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
                prntMessage = "Nice! I've marked this task as done:\n" + Messages.INDENTATION;
            } else {
                markIndexAsNotDone(index);
                prntMessage = "OK, I've marked this task as not done yet:\n" + Messages.INDENTATION;
            }
            prntMessage += items.get(index).toString();
            System.out.println(Messages.printCustomMessage(prntMessage));
        } catch (IndexOutOfBoundsException e) {
            String prntMessage = Messages.printCustomMessage("Invalid task index");
            System.out.println(prntMessage);
        }
    }

    public void printList() {
        if (items.isEmpty()) {
            System.out.println(Messages.printCustomMessage("No tasks in the list."));
            return;
        }
        String prnt = items
                .stream()
                .map(t -> String.format("%d.%s", items.indexOf(t) + 1, t))
                .reduce((x, y) -> x + "\n" + y)
                .get();
        String message = Messages.printCustomMessage(prnt);
        System.out.print(message);
    }

}