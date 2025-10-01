package task;

public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by.trim();
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    protected String extraInfo() {
        return "(by: " + by + ")";
    }

    @Override
    public String toStorageString() {
        return String.format("D | %d | %s | %s", isDone ? 1 : 0, description, by);
    }
}
