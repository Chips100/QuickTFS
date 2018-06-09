package quicktfs.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for StringUtilities.
 */
public class StringUtilitiesTests {
    /**
     * isNullOrEmpty should return true for the empty String.
     */
    @Test
    public void StringUtilities_isNullOrEmpty_Empty() {
        assertEquals(true, StringUtilities.isNullOrEmpty(""));
    }

    /**
     * isNullOrEmpty should return true for null.
     */
    @Test
    public void StringUtilities_isNullOrEmpty_Null() {
        assertEquals(true, StringUtilities.isNullOrEmpty(null));
    }

    /**
     * isNullOrEmpty should return false for any String with content.
     */
    @Test
    public void StringUtilities_isNullOrEmpty_NotNullOrEmpty() {
        assertEquals(false, StringUtilities.isNullOrEmpty("a"));
        assertEquals(false, StringUtilities.isNullOrEmpty("ab"));
        assertEquals(false, StringUtilities.isNullOrEmpty("abc"));
    }
}