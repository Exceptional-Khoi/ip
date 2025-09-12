package task;

public abstract class Task {
    protected final String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return isDone ? "X" : " "; //X if the task is done, blank if the task isn't
    }

    protected abstract String getTypeIcon();

    protected String extraInfo() { return "";}

    @Override
    public String toString() {
        String base = String.format("[%s][%s] %s", getTypeIcon(), getStatusIcon(), description);
        String extra = extraInfo();
        return extra.isEmpty() ? base : base + " " + extra;
    }
}
