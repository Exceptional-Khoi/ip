package storage;

import task.Deadline;
import task.Event;
import task.Task;
import task.Todo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing of {@link Task} data to and from a persistent text file.
 * <p>
 * The {@code Storage} class allows the Starou application to save user tasks
 * and restore them in future sessions. Tasks are serialized in a simple
 * human-readable pipe-separated format.
 * </p>
 *
 * <p><b>Example format:</b></p>
 * <pre>
 * T | 1 | Read book
 * D | 0 | Submit report | 2025-10-12 23:59
 * E | 1 | Conference | 2025-10-14 09:00 | 2025-10-14 17:00
 * </pre>
 */
public class Storage {
    private final Path filePath;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Constructs a new {@code Storage} object for managing task data at the specified path.
     *
     * @param relativePath the relative file path (e.g., {@code "./data/Starou.txt"})
     */
    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    /**
     * Loads tasks from the save file.
     * <p>
     * If the file does not exist or is empty, an empty list is returned.
     * Each line in the file is parsed into a corresponding {@link Task}
     * instance such as {@link Todo}, {@link Deadline}, or {@link Event}.
     * </p>
     *
     * @return a list of tasks loaded from the file (never {@code null})
     */
    public ArrayList<Task> load() {
        ArrayList<Task> list = new ArrayList<>();
        if(!Files.exists(filePath)) return list;

        try(BufferedReader br = Files.newBufferedReader(filePath)) {
            String line;
            while((line = br.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty()) continue;
                Task t = parseLine(line);
                if(t != null) list.add(t);
            }
        } catch (IOException e) {
            System.err.println("[WARN] Failed to read save file: " + e.getMessage());
        }
        return list;
    }

    /**
     * Parses a single line of task data from the save file into a {@link Task} object.
     * <p>
     * The expected format for each task is:
     * <ul>
     *     <li>{@code T | 1 | description}</li>
     *     <li>{@code D | 0 | description | by}</li>
     *     <li>{@code E | 1 | description | from | to}</li>
     * </ul>
     * </p>
     * Lines that do not match these formats are ignored.
     *
     * @param line a single line from the save file
     * @return the parsed {@link Task}, or {@code null} if the line is invalid or corrupted
     */
    private Task parseLine(String line) {
        try {
            String [] parts = line.split("\\s*\\|\\s*");
            if(parts.length < 3) return null;

            char type = parts[0].charAt(0);
            boolean done = parts[1].equals("1");

            switch(type) {
                case 'T': {
                    Todo t = new Todo(parts[2]);
                    if(done) t.mark();
                    return t;
                }
                case 'D': {
                    if(parts.length < 4) return null;
                    LocalDateTime by = LocalDateTime.parse(parts[3].trim(), formatter);

                    Deadline d = new Deadline(parts[2], by);
                    if (done) d.mark();
                    return d;
                }
                case 'E' : {
                    if(parts.length < 5) return null;
                    LocalDateTime from = LocalDateTime.parse(parts[3].trim(), formatter);
                    LocalDateTime to = LocalDateTime.parse(parts[4].trim(), formatter);

                    Event e = new Event(parts[2], from, to);
                    if (done) e.mark();
                    return e;
                }
                default:
                    return null;
            }
        } catch (Exception ex) {
            //Skip corrupted line
            System.err.println("[WARN] Skip corrupted line: " + line);
            return null;
        }
    }

    /**
     * Saves the provided list of tasks to the save file.
     * <p>
     * The file will be created if it does not exist, and any existing content
     * will be overwritten. Each task is serialized using its
     * {@link Task#toStorageString()} method.
     * </p>
     *
     * @param tasks the list of tasks to save
     * @throws RuntimeException if an I/O error occurs while writing to the file
     */
    public void save(List<Task> tasks) {
        try {
            if(filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            try(BufferedWriter bw = Files.newBufferedWriter(filePath)) {
                for(Task t : tasks) {
                    bw.write(t.toStorageString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("[WARN] Failed to save tasks: " + e.getMessage(), e);
        }
    }
}
