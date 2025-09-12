package task;
import exception.*;

public class Parser {

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
        String by = parts[1].trim();

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

        String[] parts1 = rest.split("/from", 2);
        String desc = parts1[0].trim();

        String[] parts2 = parts1[1].split("/to", 2);
        String from = parts2[0].trim();
        String to = (parts2.length > 1) ? parts2[1].trim() : "";

        if (desc.isEmpty()) {
            throw new InvalidCommandException("Please enter a description !");
        }
        if (from.isEmpty()) {
            throw new InvalidCommandException("Please enter a value after '/from'!");
        }
        if (to.isEmpty()) {
            throw new InvalidCommandException("Please enter a value after '/to'!");
        }

        return new Event(desc, from, to);
    }

}
