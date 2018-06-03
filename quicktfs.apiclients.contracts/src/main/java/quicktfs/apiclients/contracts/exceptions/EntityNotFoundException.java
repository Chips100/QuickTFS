package quicktfs.apiclients.contracts.exceptions;

/**
 * Thrown when an operation failed because an entity could not be found.
 */
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException() {
        super("The specified entity could not be found.", null);
    }
}