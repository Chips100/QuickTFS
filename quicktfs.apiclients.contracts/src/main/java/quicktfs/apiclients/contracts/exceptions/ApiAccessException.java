package quicktfs.apiclients.contracts.exceptions;

/**
 * Thrown when errors occur during access of the server.
 */
public class ApiAccessException extends Exception {
    /**
     * Creates an ApiAccessException.
     * @param cause Underlying cause of the exception.
     */
    public ApiAccessException(Exception cause) {
        this("Error calling TFS Server.", cause);
    }

    /**
     * Creates an ApiAccessException with a custom message.
     * @param message Message of the exception.
     * @param cause Underlying cause of the exception.
     */
    public ApiAccessException(String message, Exception cause) {
        super(message, cause);
    }
}