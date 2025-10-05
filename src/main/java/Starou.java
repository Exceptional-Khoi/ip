import exception.InvalidCommandException;
import exception.StarouException;
import storage.Storage;
import task.Deadline;
import task.Parser;
import task.Task;
import task.Todo;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class of the Starou chatbot application.
 * <p>
 * Starou is a simple command-line task management assistant that supports adding,
 * listing, deleting, marking, unmarking, and finding tasks. It also saves and loads tasks
 * from persistent storage.
 * </p>
 */

public class Starou {
    private static final String LINE = " _____________________________________________________";

    /**
     * Prints a formatted box around the given lines of text.
     * <p>
     * Each line is prefixed with two spaces, and surrounded by horizontal borders.
     * This helps format the chatbot responses for better readability.
     * </p>
     *
     * @param lines one or more strings to print inside the box
     */
    private static void printBox(String ... lines) {
        System.out.println(LINE);
        for(String s : lines) {
            System.out.println("  " + s);
        }
        System.out.println(LINE);
    }

    /**
     * The main entry point of the Starou chatbot.
     * <p>
     * Initializes the application, loads existing tasks from file storage, and listens
     * for user input via the console until the user types {@code bye}.
     * Supported commands include: {@code list}, {@code mark}, {@code unmark},
     * {@code delete}, {@code find}, {@code todo}, {@code deadline}, and {@code event}.
     * </p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        String logo = """
   _____ _
  / ____| |
 | (___ | |_ __ _ _ __ ___  _   _
  \\___ \\| __/ _` | '__/ _ \\| | | |
  ____) | || (_| | | | (_) | |_| |
 |_____/ \\__\\__,_|_|  \\___/ \\__,_|
""";

        System.out.println("Hello from\n" + logo);
        printBox("Hello! I'm Starou from Vietnam", "What can I do for you?");

        //Level 7: Create Storage
        Storage storage = new Storage("./data/Starou.txt");

        // Create a list for task
        ArrayList<Task> tasks = storage.load();
        Scanner sc = new Scanner(System.in);

        //Infinite loop until inputting "bye"
        while (true) {
            String input = sc.nextLine().trim();
            //catch error
            try {
                //enter "bye" to exit the chat
                if(input.equals("bye")) {
                    printBox("Bye! Tam biet!!");
                    break;
                }
                //List of all tasks
                else if (input.equals("list")) { //list of all tasks
                    handleList(tasks);
                }

                //Mark/ Unmark
                else if (input.startsWith("mark") || input.startsWith("unmark")) {
                    handleMarking(tasks, input);
                    storage.save(tasks);
                }

                else if (input.startsWith("delete")) {
                    handleDelete(tasks, input);
                    storage.save(tasks);
                }

                //Level 9: find
                else if (input.startsWith("find")) {
                    String keyword = Parser.parseFindCommand(input);
                    System.out.println("Here are the matching tasks in your list:");
                    int index = 1;
                    for(Task t : tasks) {
                        if(t.description.toLowerCase().contains(keyword.toLowerCase())) {
                            System.out.println(index + ". " + t);
                        }
                        index++;
                    }
                }

                else if (Parser.isAddCommand(input)) {
                    handleAdd(tasks, input);
                    storage.save(tasks);
                }

                //throw error: empty input
                else if (input.isEmpty()) {
                    throw new InvalidCommandException("Please enter a valid command!");
                }

                //Error: unknown format
                else {
                    throw new InvalidCommandException(
                            "Unknown command. Try: list, todo, deadline, event, mark, unmark, delete bye.");
                }
            } catch (StarouException e) {
                printBox(e.getMessage());
            } catch (IndexOutOfBoundsException e) {
                printBox("Invalid task index! Please enter number between 1 and " + tasks.size() + "!");
            } catch (NumberFormatException e) {
                printBox("Task index must be a positive interger!");
            } catch(Exception e) {
                printBox("Unknown error! " + e.getMessage());
            }


        }

        sc.close();
    }

    /**
     * Handles the {@code list} command by printing all tasks in the current list.
     *
     * @param tasks the list of tasks currently managed by Starou
     */
    private static void handleList(ArrayList<Task> tasks) {
        if(tasks.isEmpty()) {
            printBox("There is no task.");
        } else {
            String[] lines = new String[tasks.size()];
            for (int i = 0; i < tasks.size(); i++) {
                lines[i] = (i + 1) + ". " + tasks.get(i);
            }
            printBox(lines);
        }
    }

    /**
     * Handles {@code mark} and {@code unmark} commands to update the completion status of a task.
     *
     * @param tasks the list of tasks currently managed by Starou
     * @param input the full user input, expected to contain the task index
     * @throws InvalidCommandException if the command format is invalid or the index is out of range
     */
    private static void handleMarking(ArrayList <Task> tasks, String input) {
        boolean isMark = input.startsWith("mark ");
        String[] parts = input.split("\\s+");

        //error: didn't enter task index
        if(parts.length < 2) {
            throw new InvalidCommandException("Command lacks task index!");
        }
        int index = Integer.parseInt(parts[1]);
        if(index <= 0 || index > tasks.size()) {
            throw new InvalidCommandException("Index must be between 1 and " + tasks.size() + "!");
        }

        Task t = tasks.get(index - 1);
        if(isMark) {
            t.mark();
            printBox("I've mark this task as done:", " " + t);
        } else {
            t.unmark();
            printBox("I've mark this task as not done yet:", " " + t);
        }
    }

    /**
     * Handles adding a new task (e.g., {@code todo}, {@code deadline}, or {@code event})
     * to the task list.
     *
     * @param tasks the list of tasks currently managed by Starou
     * @param input the full user input containing the add command and its details
     */
    public static void handleAdd(ArrayList<Task> tasks, String input) {
        Task t = Parser.parseAddCommand(input);
        tasks.add(t);
        String kind = t instanceof Todo ? "task"
                : t instanceof Deadline ? "deadline"
                : "event";
        printBox("Got it. I've add this " + kind + ":", " " + t.toString(), "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Handles the {@code delete} command to remove a task from the list.
     *
     * @param tasks the list of tasks currently managed by Starou
     * @param input the full user input containing the delete command and task index
     * @throws InvalidCommandException if the command format is invalid or the index is out of range
     */
    private static void handleDelete(ArrayList<Task> tasks, String input) {
        String[] parts = input.split("\\s+");
        if (parts.length < 2) throw new InvalidCommandException("Command lacks task index!");

        int index = Integer.parseInt(parts[1]);
        if(index <= 0 || index > tasks.size()) {
            throw new InvalidCommandException("Index must be between 1 and " + tasks.size() + "!");
        }

        Task removed = tasks.remove(index - 1);
        printBox("Noted. I've removed this task:",
                "  " + removed.toString(),
                "Now you have " + tasks.size() + " tasks in the list.");
    }
}