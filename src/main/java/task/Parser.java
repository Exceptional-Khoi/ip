package task;
import exception.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    //return true if input start with todo, deadline, event
    public static boolean isAddCommand(String input) {
        if (input == null) return false;
        String s = input.trim();
        return s.startsWith("todo") || s.startsWith("deadline") || s.startsWith("event");
    }

    public static Task parseAddCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidCommandException("Please enter a valid command!");
        }

        String s = input.trim();

        if (s.startsWith("todo")) {
            return parseTodo(s);
        } else if (s.startsWith("deadline")) {
            return parseDeadline(s);
        } else if (s.startsWith("event")) {
            return parseEvent(s);
        } else {
            throw new InvalidCommandException("Unknown command! Try: todo, deadline, event!");
        }
    }

    private static Task parseTodo(String s) {
        String desc = s.substring("todo".length()).trim();
        if (desc.isEmpty()) {
            throw new InvalidCommandException("Please enter task description after todo!");
        }
        return new Todo(desc);
    }

    private static Task parseDeadline(String s) {
        String rest = s.substring("deadline".length()).trim();
        if (rest.isEmpty()) {
            throw new InvalidCommandException("Deadline needs a description and '/by <time>' !");
        }

        if (!rest.contains("/by")) {
            throw new InvalidCommandException("Deadline needs '/by <time>' !");
        }

        String[] parts = rest.split("/by", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new InvalidCommandException("Please enter a value after '/by' !");
        }

        String desc = parts[0].trim();
        LocalDateTime by = LocalDateTime.parse(parts[1].trim(), formatter);

        if (desc.isEmpty()) {
            throw new InvalidCommandException("Deadline description cannot be empty.");
        }

        return new Deadline(desc, by);
    }

    private static Task parseEvent(String s) {
        String rest = s.substring("event".length()).trim();
        if (rest.isEmpty()) {
            throw new InvalidCommandException("Event needs a description and '/from ... /to ...' !");
        }

        if (!rest.contains("/from") || !rest.contains("/to")) {
            throw new InvalidCommandException("Event needs '/from ... /to ...' !");
        }

        String[] parts = rest.split("/from|/to");
        String desc = parts[0].replaceFirst("event", "").trim();
        LocalDateTime from = LocalDateTime.parse(parts[1].trim(), formatter);
        LocalDateTime to = LocalDateTime.parse(parts[2].trim(), formatter);

        if (desc.isEmpty()) {
            throw new InvalidCommandException("Please enter a description !");
        }

        return new Event(desc, from, to);
    }

}
