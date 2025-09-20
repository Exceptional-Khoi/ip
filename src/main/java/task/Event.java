package task;

public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from.trim();
        this.to = to.trim();
    }

    @Override
    protected String getTypeIcon() {
        return "E";
    }

    @Override
    protected String extraInfo() {
        return "(from: "  + from + " to: " + to + ")";
    }

    @Override
    public String toStorageString() {
        return String.format("E | %d | %s | %s | %s", isDone ? 1 : 0, description, from, to);
    }
}
