package quicktfs.apiclients.contracts;

/**
 * Thrown when an operation has been attempted that requires
 * being logged in without being logged in.
 */
public class NotAuthenticatedException extends ApiAccessException {
    public NotAuthenticatedException() {
        super("Login required.", null);
    }
}