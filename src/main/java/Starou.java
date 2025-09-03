import java.util.ArrayList;
import java.util.Scanner;

public class Starou {
    private static String LINE = " _____________________________________________________";

    //Function to print the chat box
    private static void printBox(String ... lines) {
        System.out.println(LINE);
        for(String s : lines) {
            System.out.println("  " + s);
        }
        System.out.println(LINE);
    }

    public static void main(String[] args) {
        String logo =
                "   _____ _                        \n"
                        + "  / ____| |                       \n"
                        + " | (___ | |_ __ _ _ __ ___  _   _ \n"
                        + "  \\___ \\| __/ _` | '__/ _ \\| | | |\n"
                        + "  ____) | || (_| | | | (_) | |_| |\n"
                        + " |_____/ \\__\\__,_|_|  \\___/ \\__,_|\n";
        System.out.println("Hello from\n" + logo);
        printBox("Hello! I'm Starou from Vietnam", "What can I do for you?");

        // Create a list for task
        ArrayList<Task> tasks = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        //Infinite loop until inputting "bye"
        while (true) {
            String input = sc.nextLine().trim();

            //enter "bye" to exit the chat
            if(input.equals("bye")) {
                printBox("Bye! Tam biet!!");
                break;
            }
            //List of all tasks
            else if (input.equals("list")) { //list of all tasks
                if (tasks.isEmpty()) {
                    printBox("There is no task yet!");
                } else {
                    String[] lines = new String[tasks.size()];
                    for (int i = 0; i < tasks.size(); i++) {
                        lines[i] = (i + 1) + ". " + tasks.get(i);
                    }
                    printBox(lines);
                }
            }

            //Mark/ Unmark
            else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                boolean isMark = input.startsWith("mark ");
                int index = Integer.parseInt(input.split("\\s+")[1]);
                Task t = tasks.get(index - 1);

                if(isMark) {
                    t.mark();
                    printBox("I've mark this task as done:", " " + t.toString());
                } else {
                    t.unmark();
                    printBox("I've mark this task as not done yet:", " " + t.toString());
                }
            }

            //Add new task to the list
            else {
                tasks.add(new Task(input));
                printBox("added: " + input);
            }
        }

        sc.close();
    }
}