package quicktfs.utilities;

/**
 * Utility methods for dealing with exceptions.
 */
public class ExceptionUtilities {
    /**
     * Searches the underlying causes for the specified type.
     * @param exception Exception with the causes that should be searched through.
     * @param innerExceptionClass Type of the underlying cause to search.
     * @param <T> Type of the underlying cause to search.
     * @return The underlying cause of the specified type; or null if it was not found.
     */
    @SuppressWarnings("unchecked") // Casts are checked for safety.
    public static<T> T findCauseOfType(Throwable exception, Class<T> innerExceptionClass) {
        // Recursion exit condition: End of inner exceptions reached.
        if (exception == null) return null;

        // Check if exception itself is of the specified type.
        if (exception.getClass() == innerExceptionClass) return (T)exception;

        // Otherwise search chain of inner exceptions recursively.
        return findCauseOfType(exception.getCause(), innerExceptionClass);
    }
}