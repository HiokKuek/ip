package wader.task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DeadlineTask extends Task {
    private LocalDate date;
    private LocalTime time;

    public DeadlineTask(String description, String dateString, String timeString) {
        super(description);
        this.date = LocalDate.parse(dateString);
        this.time = LocalTime.parse(timeString);
    }

    /**
     * Gets the date of the deadline task.
     *
     * @return The formatted date string in "MMM d yyyy" format.
     */
    public String getDate() {
        return date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    /**
     * Gets the time of the deadline task.
     *
     * @return The formatted time string in "ha" format e.g. "5pm".
     */

    public String getTime() {
        return time.format(DateTimeFormatter.ofPattern("ha"));
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), getDate() + " " + getTime());
    }

}
