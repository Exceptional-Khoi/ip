package exception;

/**
 * Thrown to indicate that a user command entered into the Starou chatbot
 * is invalid or incorrectly formatted.
 * <p>
 * This exception is typically raised by the {@code Parser} class when
 * a command does not follow the expected syntax or lacks required arguments.
 * </p>
 *
 * @see StarouException
 * @see task.Parser
 */
public class StarouException extends RuntimeException {
    /**
     * Constructs a new {@code InvalidCommandException} with the specified detail message.
     *
     * @param message the detail message describing the cause of the error
     */
    public StarouException(String message) {
        super(message);
    }
}
