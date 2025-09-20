package task;

public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String getTypeIcon() {
        return "T";
    }

    @Override
    public String toStorageString() {
        return String.format("T | %d | %s", isDone ? 1 : 0, description);
    }
}
