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
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage(String relativePath) {
        this.filePath = Paths.get(relativePath);
    }

    //Load tasks from file. If file doesn't exist -> return empty list
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

    /* Parse 1 line following the format:
     *  T | 1 | desc
     *  D | 0 | desc | by
     *  E | 1 | desc | from | to
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
                    Deadline d = new Deadline(parts[2], parts[3]);
                    if (done) d.mark();
                    return d;
                }
                case 'E' : {
                    if(parts.length < 5) return null;
                    Event e = new Event(parts[2], parts[3], parts[4]);
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

    //Write on files from current tasks
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
