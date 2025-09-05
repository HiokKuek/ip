package wader.task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventTask extends Task {
    private LocalDate toDate;
    private LocalTime toTime;

    private LocalDate fromDate;
    private LocalTime fromTime;

    public EventTask(String description, String fromTimeString, String toTimeString,
            String fromDateString, String toDateString) {
        super(description);
        this.toDate = LocalDate.parse(toDateString);
        this.toTime = LocalTime.parse(toTimeString);
        this.fromDate = LocalDate.parse(fromDateString);
        this.fromTime = LocalTime.parse(fromTimeString);
    }

    /**
     * Gets the from time of the event task.
     *
     * @return The formatted from time string in "ha" format e.g. "5pm".
     */
    public String getFromTime() {
        return fromTime.format(DateTimeFormatter.ofPattern("ha"));
    }

    /**
     * Gets the to time of the event task.
     *
     * @return The formatted to time string in "ha" format e.g. "5pm".
     */
    public String getToTime() {
        return toTime.format(DateTimeFormatter.ofPattern("ha"));
    }

    /**
     * Gets the from date of the event task.
     *
     * @return The formatted from date string in "MMM d yyyy" format.
     */

    public String getFromDate() {
        return fromDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    /**
     * Gets the to Date of the event task.
     *
     * @return The formatted to date string in "MMM d yyyy" format.
     */
    public String getToDate() {
        return toDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                getFromDate() + " " + getFromTime(), getToDate() + " " + getToTime());
    }

}
