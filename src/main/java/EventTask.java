import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class EventTask extends Task {
    private LocalDate toDate;
    private LocalTime toTime;

    private LocalDate fromDate;
    private LocalTime fromTime;

    public EventTask(String description, String fromTimeString, String toTimeString, String fromDateString,
            String toDateString) {
        super(description);
        this.toDate = LocalDate.parse(toDateString);
        this.toTime = LocalTime.parse(toTimeString);
        this.fromDate = LocalDate.parse(fromDateString);
        this.fromTime = LocalTime.parse(fromTimeString);
    }

    public String getFromTime() {
        return fromTime.format(DateTimeFormatter.ofPattern("ha"));
    }

    public String getToTime() {
        return toTime.format(DateTimeFormatter.ofPattern("ha"));
    }

    public String getFromDate() {
        return fromDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    public String getToDate() {
        return toDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                getFromDate() + " " + getFromTime(),
                getToDate() + " " + getToTime());
    }

}
