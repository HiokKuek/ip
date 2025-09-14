package wader.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import wader.task.DeadlineTask;
import wader.task.EventTask;
import wader.task.Task;
import wader.task.ToDoTask;

public class WaderList {
    private List<Task> items;

    public WaderList() {
        items = new ArrayList<>();
    }

    public Task addToDoTask(String desc) {
        Task task = new ToDoTask(desc);
        items.add(task);
        return task;
    }

    public Task addDeadlineTask(String desc, String deadline) throws DukeException {
        Task task;
        try {
            String[] parts = deadline.split(" ");
            if (parts.length != 2) {
                throw new DateTimeParseException("Invalid deadline format", deadline, 0);
            }
            String date = parts[0];
            String time = parts[1];
            task = new DeadlineTask(desc, date, time);
            items.add(task);
        } catch (DateTimeParseException e) {
            throw new DukeException("Invalid deadline format. Please use 'date time' format.");
        }
        return task;
    }

    public Task addEventTask(String desc, String from, String to) throws DukeException {
        Task task;
        try {
            String[] fromParts = from.split(" ");
            String[] toParts = to.split(" ");
            if (fromParts.length != 2 || toParts.length != 2) {
                throw new DateTimeParseException("Invalid event format", from + " " + to, 0);
            }
            String fromDateString = fromParts[0];
            String toDateString = toParts[0];
            String fromTimeString = fromParts[1];
            String toTimeString = toParts[1];

            task = new EventTask(desc, fromTimeString, toTimeString, fromDateString, toDateString);
            items.add(task);

        } catch (DateTimeParseException e) {
            throw new DukeException("Invalid event format. Please use 'date time' format.");
        }
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

    public List<Task> findTasks(String keyword) {
        return items.stream().filter(task -> task.getDescription().contains(keyword)).toList();
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

    /**
     * Gets the next upcoming tasks with dates, sorted by their date/time.
     * Only returns tasks that have dates (deadline and event tasks).
     *
     * @param count the maximum number of upcoming tasks to return
     * @return a list of the next upcoming tasks, up to the specified count
     */
    public List<Task> getNextUpcomingTasks(int count) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        return items.stream()
                .filter(Task::hasDate) // Only consider tasks with dates
                .filter(task -> task.getDateTime().isAfter(now)) // Only future tasks
                .sorted((t1, t2) -> t1.getDateTime().compareTo(t2.getDateTime())) // Sort by date
                .limit(count) // Get the first 'count' tasks
                .collect(java.util.stream.Collectors.toList());
    }
}
