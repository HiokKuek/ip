import java.util.ArrayList;
import java.util.List;

public class WaderList {
    List<Task> items;

    public WaderList() {
        items = new ArrayList<>();
    }

    public Task addToDoTask(String desc) {
        Task task = new ToDoTask(desc);
        items.add(task);
        return task;
    }

    public Task addDeadlineTask(String desc, String deadline) {
        Task task = new DeadlineTask(desc, deadline);
        items.add(task);
        return task;
    }

    public Task addEventTask(String desc, String from, String to) {
        Task task = new EventTask(desc, from, to);
        items.add(task);
        return task;
    }

    public Task delete(int index) throws IndexOutOfBoundsException {
        return items.remove(index);
    }

    public boolean mark(int index) {
        try {
            items.get(index).markAsDone();
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }

    public boolean unmark(int index) {
        try {
            items.get(index).markAsNotDone();
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        return true;
    }

    public List<Task> getTasks() {
        return new ArrayList<>(items);
    }

    public String getTaskString(int index) {
        return items.get(index).toString();
    }

    public int getSize() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}