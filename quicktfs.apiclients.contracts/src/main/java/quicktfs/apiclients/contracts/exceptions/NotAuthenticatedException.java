package quicktfs.apiclients.contracts.exceptions;

/**
 * Thrown when an operation has been attempted that requires
 * being logged in without being logged in.
 */
public class NotAuthenticatedException extends Exception {
    public NotAuthenticatedException() {
        super("Login required.", null);
    }
}