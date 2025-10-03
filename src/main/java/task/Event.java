package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    protected String getTypeIcon() {
        return "E";
    }

    @Override
    protected String extraInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");
        return String.format("(from: %s to %s)", from.format(formatter), to.format(formatter));
    }

    @Override
    public String toStorageString() {
        return String.format("E | %d | %s | %s | %s", isDone ? 1 : 0, description, from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")), to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
    }
}
