package quicktfs.utilities;

/**
 * Utility methods for dealing with Strings.
 */
public class StringUtilities {
    /**
     * Checks if the specified String is null or the empty String.
     * @param value Value to check.
     * @return True, if the specified String is null or the empty String; otherwise false.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.equals("");
    }
}