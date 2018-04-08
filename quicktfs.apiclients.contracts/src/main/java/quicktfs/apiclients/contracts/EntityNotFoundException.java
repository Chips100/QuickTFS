package quicktfs.apiclients.contracts;

/**
 * Thrown when an operation failed because an entity could not be found.
 */
public class EntityNotFoundException extends ApiAccessException {
    public EntityNotFoundException() {
        super("The specified entity could not be found.", null);
    }
}