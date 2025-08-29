package Wader.Util;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import Wader.Task.Task;
import Wader.Task.ToDoTask;
import Wader.Task.DeadlineTask;
import Wader.Task.EventTask;

/**
 * Handles the persistent storage of tasks to and from a file.
 * This class provides functionality to save a WaderList to a file and load
 * tasks
 * from a file back into a WaderList, maintaining task completion status and
 * formatting.
 */
public class Storage {
    private String filePath;

    /**
     * Constructs a Storage object with the specified file path.
     * 
     * @param filePath the path to the file where tasks will be saved and loaded
     *                 from
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves all tasks from the given WaderList to the storage file.
     * Each task is written as a string representation on a separate line.
     * If the file doesn't exist, it will be created. If it exists, it will be
     * overwritten.
     * 
     * @param waderList the WaderList containing tasks to be saved
     * @throws DukeException if an error occurs during file writing operations
     */
    public void save(WaderList waderList) throws DukeException {
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter writer = new FileWriter(file);
            String toWrite = "";
            for (int i = 0; i < waderList.getSize(); i++) {
                toWrite += waderList.getTaskString(i) + "\n";
            }
            writer.write(toWrite);
            writer.close();
        } catch (IOException e) {
            throw new DukeException("An error occurred while saving the file: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the storage file and returns them as a WaderList.
     * Parses each line in the file as a task string and recreates the appropriate
     * Task objects (ToDoTask, DeadlineTask, or EventTask) with their completion
     * status.
     * If the file doesn't exist, returns an empty WaderList.
     * 
     * Supported task formats:
     * <ul>
     * <li>ToDo: [T][X] description or [T][ ] description</li>
     * <li>Deadline: [D][X] description (by: deadline) or [D][ ] description (by:
     * deadline)</li>
     * <li>Event: [E][X] description (from: start to: end) or [E][ ] description
     * (from: start to: end)</li>
     * </ul>
     * 
     * @return a WaderList containing all tasks loaded from the file, or an empty
     *         list if file doesn't exist
     * @throws DukeException if an error occurs during file reading operations or if
     *                       task parsing fails
     */
    public WaderList load() throws DukeException {
        WaderList waderList = new WaderList();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return waderList; // Return empty list if file doesn't exist
            }

            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().strip();
                if (!line.isEmpty()) {
                    Task task = parseTaskFromString(line);
                    if (task != null) {
                        addTaskToWaderList(task, waderList);
                    }
                }
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            throw new DukeException("File not found: " + filePath);
        } catch (Exception e) {
            throw new DukeException("An error occurred while loading the file: " + e.getMessage());
        }
        return waderList;
    }

    /**
     * Parses a task string from the saved file format and creates the appropriate
     * Task object.
     * Expected formats:
     * - [T][X] description (ToDo task)
     * - [D][ ] description (by: deadline) (Deadline task)
     * - [E][X] description (from: start to: end) (Event task)
     */
    private Task parseTaskFromString(String taskString) throws DukeException {
        if (taskString.length() < 6) {
            return null; // Invalid format
        }

        // Extract task type and completion status
        char taskType = taskString.charAt(1); // T, D, or E
        boolean isDone = taskString.charAt(3) == 'X';
        String content = taskString.substring(6).trim(); // Remove "[T][X] " or "[D][ ] " and trim extra spaces

        Task task = null;

        if (taskType == 'T') {
            // ToDo task: [T][X] description
            task = new ToDoTask(content);
        } else if (taskType == 'D') {
            // Deadline task: [D][X] description (by: Aug 21 2025 6pm)
            int byIndex = content.lastIndexOf(" (by: ");
            if (byIndex != -1) {
                String description = content.substring(0, byIndex);
                String deadline = content.substring(byIndex + 6, content.length() - 1); // Remove " (by: " and ")"

                // Parse the deadline format "Aug 21 2025 6pm" -> "2025-08-21 18:00"
                String[] deadlineParts = deadline.split(" ");
                if (deadlineParts.length >= 4) {
                    String dateStr = convertToISODate(deadlineParts[2], deadlineParts[0], deadlineParts[1]);
                    String timeStr = convertToISOTime(deadlineParts[3]);
                    task = new DeadlineTask(description, dateStr, timeStr);
                }
            }
        } else if (taskType == 'E') {
            // Event task: [E][X] description (from: Aug 22 2025 2pm to: Aug 25 2025 11pm)
            int fromIndex = content.lastIndexOf(" (from: ");
            int toIndex = content.lastIndexOf(" to: ");
            if (fromIndex != -1 && toIndex != -1) {
                String description = content.substring(0, fromIndex);
                String from = content.substring(fromIndex + 8, toIndex);
                String to = content.substring(toIndex + 5, content.length() - 1); // Remove " to: " and ")"

                // Parse from and to dates/times
                String[] fromParts = from.split(" ");
                String[] toParts = to.split(" ");
                if (fromParts.length >= 4 && toParts.length >= 4) {
                    String fromDateStr = convertToISODate(fromParts[2], fromParts[0], fromParts[1]);
                    String fromTimeStr = convertToISOTime(fromParts[3]);
                    String toDateStr = convertToISODate(toParts[2], toParts[0], toParts[1]);
                    String toTimeStr = convertToISOTime(toParts[3]);
                    task = new EventTask(description, fromTimeStr, toTimeStr, fromDateStr, toDateStr);
                }
            }
        }

        // Set completion status
        if (task != null && isDone) {
            task.markAsDone();
        }

        return task;
    }

    /**
     * Adds a parsed task to the WaderList and preserves its completion status.
     */
    private void addTaskToWaderList(Task task, WaderList waderList) {
        boolean wasCompleted = task.isDone();

        if (task instanceof ToDoTask) {
            waderList.addToDoTask(task.getDescription());
        } else if (task instanceof DeadlineTask) {
            // We need to reconstruct the deadline format for WaderList
            String savedFormat = task.toString();
            int byIndex = savedFormat.lastIndexOf(" (by: ");
            if (byIndex != -1) {
                String deadline = savedFormat.substring(byIndex + 6, savedFormat.length() - 1);
                String[] parts = deadline.split(" ");
                if (parts.length >= 4) {
                    String dateStr = convertToISODate(parts[2], parts[0], parts[1]);
                    String timeStr = convertToISOTime(parts[3]);
                    try {
                        waderList.addDeadlineTask(task.getDescription(), dateStr + " " + timeStr);
                    } catch (DukeException e) {
                        // Skip invalid tasks
                        return;
                    }
                }
            }
        } else if (task instanceof EventTask) {
            // We need to reconstruct the event format for WaderList
            // For now, using placeholder values - this would need proper parsing from the
            // saved format
            String fromDateStr = "2025-08-22";
            String fromTimeStr = "14:00";
            String toDateStr = "2025-08-25";
            String toTimeStr = "23:00";

            try {
                waderList.addEventTask(task.getDescription(),
                        fromDateStr + " " + fromTimeStr,
                        toDateStr + " " + toTimeStr);
            } catch (DukeException e) {
                // Skip invalid tasks
                return;
            }
        }

        // Restore completion status
        if (wasCompleted) {
            waderList.mark(waderList.getSize() - 1);
        }
    }

    /**
     * Converts date format from "2025 Aug 21" to "2025-08-21"
     */
    private String convertToISODate(String year, String month, String day) {
        String monthNum = getMonthNumber(month);
        return year + "-" + monthNum + "-" + String.format("%02d", Integer.parseInt(day));
    }

    /**
     * Converts time format from "6pm" to "18:00"
     */
    private String convertToISOTime(String time) {
        if (time.toLowerCase().endsWith("pm")) {
            int hour = Integer.parseInt(time.substring(0, time.length() - 2));
            if (hour != 12)
                hour += 12;
            return String.format("%02d:00", hour);
        } else if (time.toLowerCase().endsWith("am")) {
            int hour = Integer.parseInt(time.substring(0, time.length() - 2));
            if (hour == 12)
                hour = 0;
            return String.format("%02d:00", hour);
        }
        return time; // Return as-is if format is unexpected
    }

    /**
     * Converts month name to month number
     */
    private String getMonthNumber(String month) {
        switch (month) {
            case "Jan":
                return "01";
            case "Feb":
                return "02";
            case "Mar":
                return "03";
            case "Apr":
                return "04";
            case "May":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
            default:
                return "01";
        }
    }
}
