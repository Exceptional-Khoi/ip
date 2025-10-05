package task;
import exception.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The {@code Parser} class provides utility methods to parse user input
 * into {@link Task} objects or specific command arguments.
 * <p>
 * It supports parsing commands for creating {@link Todo}, {@link Deadline},
 * and {@link Event} tasks, as well as finding keywords in the task list.
 * Invalid or malformed commands result in an {@link InvalidCommandException}.
 * </p>
 */
public class Parser {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Determines whether a user input string represents a valid add command.
     * <p>
     * A command is considered an "add" command if it starts with
     * {@code todo}, {@code deadline}, or {@code event}.
     * </p>
     *
     * @param input the raw user input string
     * @return {@code true} if the input starts with an add command keyword; {@code false} otherwise
     */
    public static boolean isAddCommand(String input) {
        if (input == null) return false;
        String s = input.trim();
        return s.startsWith("todo") || s.startsWith("deadline") || s.startsWith("event");
    }

    /**
     * Parses a user input string and constructs the corresponding {@link Task} object.
     * <p>
     * Supports the following formats:
     * <ul>
     *     <li>{@code todo <description>}</li>
     *     <li>{@code deadline <description> /by <yyyy-MM-dd HH:mm>}</li>
     *     <li>{@code event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>}</li>
     * </ul>
     * </p>
     *
     * @param input the full user command string
     * @return a {@link Task} instance of type {@link Todo}, {@link Deadline}, or {@link Event}
     * @throws InvalidCommandException if the command format is invalid or unsupported
     */
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

    /**
     * Parses a {@code todo} command and constructs a {@link Todo} task.
     *
     * @param s the user input starting with {@code todo}
     * @return a new {@link Todo} task
     * @throws InvalidCommandException if the description is missing or empty
     */
    private static Task parseTodo(String s) {
        String desc = s.substring("todo".length()).trim();
        if (desc.isEmpty()) {
            throw new InvalidCommandException("Please enter task description after todo!");
        }
        return new Todo(desc);
    }

    /**
     * Parses a {@code deadline} command and constructs a {@link Deadline} task.
     * <p>
     * Expected format:
     * {@code deadline <description> /by <yyyy-MM-dd HH:mm>}
     * </p>
     *
     * @param s the user input starting with {@code deadline}
     * @return a new {@link Deadline} task with its due date
     * @throws InvalidCommandException if description or {@code /by} clause is missing or invalid
     */
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

    /**
     * Parses an {@code event} command and constructs an {@link Event} task.
     * <p>
     * Expected format:
     * {@code event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>}
     * </p>
     *
     * @param s the user input starting with {@code event}
     * @return a new {@link Event} task with start and end times
     * @throws InvalidCommandException if description, {@code /from}, or {@code /to} parts are missing or invalid
     */
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
            throw new InvalidCommandException("Please enter a description!");
        }

        return new Event(desc, from, to);
    }

    /**
     * Extracts the keyword from a {@code find} command for searching tasks.
     * <p>
     * Expected format:
     * {@code find <keyword>}
     * </p>
     *
     * @param input the full user command string
     * @return the keyword following the {@code find} command
     * @throws InvalidCommandException if the keyword is missing or empty
     */
    public static String parseFindCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidCommandException("Please enter a valid keyword!");
        }

        String keyword = input.substring("find".length()).trim();
        if (keyword.isEmpty()) {
            throw new InvalidCommandException("Please enter a keyword after 'find'!");
        }

        return keyword;
    }


}
