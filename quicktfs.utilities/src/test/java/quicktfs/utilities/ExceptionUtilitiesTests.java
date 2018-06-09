package quicktfs.utilities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for ExceptionUtilities.
 */
public class ExceptionUtilitiesTests {

    /**
     * findCauseOfType should return the exception itself if
     * the type matches.
     */
    @Test
    public void ExceptionUtilities_findCauseOfType_selfMatch() {
        ExceptionTwo rootException = new ExceptionTwo(null);

        assertSame(rootException, ExceptionUtilities.findCauseOfType(rootException, ExceptionTwo.class));
    }

    /**
     * findCauseOfType should check the cause of the given exception.
     */
    @Test
    public void ExceptionUtilities_findCauseOfType_nestedLevelOne() {
        ExceptionTwo nestedException = new ExceptionTwo(null);
        ExceptionOne rootException = new ExceptionOne(nestedException);

        assertSame(nestedException, ExceptionUtilities.findCauseOfType(rootException, ExceptionTwo.class));
    }

    /**
     * findCauseOfType should check continue searching the causes
     * of the causes of the given exception.
     */
    @Test
    public void ExceptionUtilities_findCauseOfType_nestedLevelTwo() {
        ExceptionTwo nestedNestedException = new ExceptionTwo(null);
        ExceptionOne nestedException = new ExceptionOne(nestedNestedException);
        ExceptionOne rootException = new ExceptionOne(nestedException);

        assertSame(nestedNestedException, ExceptionUtilities.findCauseOfType(rootException, ExceptionTwo.class));
    }

    /**
     * findCauseOfType should return null if no cause
     * of the specfied type was found.
     */
    @Test
    public void ExceptionUtilities_findCauseOfType_noMatch() {
        ExceptionOne nestedNestedException = new ExceptionOne(null);
        ExceptionOne nestedException = new ExceptionOne(nestedNestedException);
        ExceptionOne rootException = new ExceptionOne(nestedException);

        assertSame(null, ExceptionUtilities.findCauseOfType(rootException, ExceptionTwo.class));
    }

    private class ExceptionOne extends Exception {
        public ExceptionOne(Throwable cause) {
            super(cause);
        }
    }

    private class ExceptionTwo extends Exception {
        public ExceptionTwo(Throwable cause) {
            super(cause);
        }
    }
}