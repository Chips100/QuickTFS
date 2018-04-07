package quicktfs.apiclients.contracts;

/**
 * Thrown when errors occur during access of the server.
 */
public class ApiAccessException extends Exception {
    /**
     * Creates an ApiAccessException.
     * @param cause Underlying cause of the exception.
     */
    public ApiAccessException(Exception cause) {
        super("Error calling TFS Server.", cause);
    }
}