import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

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
                    // For now, we'll skip loading from file since the format is complex
                    // This can be implemented later when we have a standardized save format
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
}
