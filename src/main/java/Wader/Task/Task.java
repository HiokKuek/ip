package wader.task;

import java.time.LocalDateTime;

public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Gets the description of a Task
     *
     * @return The description of the Task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the status of a Task
     *
     * @return The status of the Task
     */

    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the Task as done
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the Task as not done
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns a string representation of the Task e.g. [E][X] event (from: Jan 1
     * 2022 5pm to: Jan 2
     * 2022 6pm)
     */
    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }

    /**
     * Checks if this task has a date associated with it.
     * Default implementation returns false for base Task class.
     *
     * @return true if task has a date, false otherwise
     */
    public boolean hasDate() {
        return false;
    }

    /**
     * Gets the date associated with this task.
     * Default implementation throws UnsupportedOperationException.
     *
     * @return the LocalDateTime associated with this task
     * @throws UnsupportedOperationException if the task does not have a date
     */
    public java.time.LocalDateTime getDateTime() {
        throw new UnsupportedOperationException("This task does not have a date.");
    }

}
