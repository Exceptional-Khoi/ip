package task;

/**
 * Represents an abstract task in the Starou task management system.
 * <p>
 * A {@code Task} stores a textual description and whether it has been completed.
 * Subclasses such as {@link Todo}, {@link Deadline}, and {@link Event}
 * must define their specific type icon and storage format.
 * </p>
 */
public abstract class Task {
    public final String description;
    protected boolean isDone;

    /**
     * Constructs a new {@code Task} with the given description.
     * The task is initially marked as not done.
     *
     * @param description the textual description of the task
     */
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

    /**
     * Returns the status icon representing the task’s completion state.
     * <p>
     * The icon is {@code "X"} if the task is done, or a single space {@code " "}
     * if it is not done.
     * </p>
     *
     * @return the completion status icon
     */
    public String getStatusIcon() {
        return isDone ? "X" : " "; //X if the task is done, blank if the task isn't
    }

    /**
     * Returns the type icon that identifies the subclass of the task.
     * <p>
     * Implementations must define a one-character symbol representing
     * their task type (e.g., {@code "T"} for Todo, {@code "D"} for Deadline).
     * </p>
     *
     * @return a single-character type icon
     */
    protected abstract String getTypeIcon();

    /**
     * Returns additional information for display, if applicable.
     * <p>
     * Subclasses may override this to append extra details such as
     * a deadline or event date.
     * </p>
     *
     * @return a string containing extra task information, or an empty string if none
     */
    protected String extraInfo() { return "";}

    /**
     * Returns a string representation of this task for saving to persistent storage.
     * <p>
     * Each subclass must define how its information is serialized
     * (e.g., type, completion status, and any extra data).
     * </p>
     *
     * @return the formatted storage string for this task
     */
    public abstract String toStorageString();

    /**
     * Returns a user-friendly string representation of this task.
     * <p>
     * The format includes the type icon, completion status, and description,
     * followed by any extra info defined by {@link #extraInfo()}.
     * </p>
     *
     * @return a string representation suitable for display in the console
     */
    @Override
    public String toString() {
        String base = String.format("[%s][%s] %s", getTypeIcon(), getStatusIcon(), description);
        String extra = extraInfo();
        return extra.isEmpty() ? base : base + " " + extra;
    }
}
