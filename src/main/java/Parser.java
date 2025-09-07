public class Parser {
    private Parser() {}

    public static Task parseAddCommand(String input) {
        String lower = input.toLowerCase();

        if(lower.startsWith("todo ")) {
            String desc = input.substring(5).trim();
            return new Todo(desc);
        }

        if(lower.startsWith("deadline ")) {
            String body = input.substring(9).trim();
            int idx = body.toLowerCase().lastIndexOf("/by ");
            String desc = body.substring(0, idx).trim();
            String by = body.substring(idx + 4).trim();
            return new Deadline(desc, by);
        }

        if(lower.startsWith("event ")) {
            String body = input.substring(6).trim();
            int idxFrom = body.toLowerCase().lastIndexOf("/from ");
            int idxTo = body.toLowerCase().lastIndexOf("/to ");
            String desc = body.substring(0, idxFrom).trim();
            String from = body.substring(idxFrom + 6, idxTo).trim();
            String to = body.substring(idxTo + 4).trim();
            return new Event(desc, from, to);
        }

        return null;
    }

    public static boolean isAddCommand(String input) {
        String s = input.toLowerCase();
        return s.startsWith("todo ") || s.startsWith("deadline ") || s.startsWith("event ");
    }
}
