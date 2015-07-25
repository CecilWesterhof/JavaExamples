package nl.decebal.validator;


import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import        org.junit.Test;


// public
public class NotEmptyTest {
    // public
    /*
     * We need three type of tests:
     * - Filled string
     * - Empty  string
     * - Null   string
     *
     * They are tested with default message and own message
     */
    // The three tests for @NotEmpty with own message
    @Test
    public void emptyStringTestOwnMessage() {
        checkNotEmptyClass("", true, true);
    }

    @Test
    public void filledStringTestOwnMessage() {
        checkNotEmptyClass(filledString, false, true);
    }

    @Test
    public void nullStringTestOwnMessage() {
        checkNotEmptyClass(null, true, true);
    }

    // The three tests for @NotEmpty with default message
    @Test
    public void emptyStringTestDefaultMessage() {
        checkNotEmptyClass("", true, false);
    }

    @Test
    public void filledStringTestDefaultMessage() {
        checkNotEmptyClass(filledString, false, false);
    }

    @Test
    public void nullTestDefaultMessage() {
        checkNotEmptyClass(null, true, false);
    }


    // private
    // Class with own message
    private class NotEmptyClassOwnMessage {
        @SuppressWarnings("unused")
        @NotEmpty(message = errorMessage)
        String notEmpty;

        NotEmptyClassOwnMessage(String value) {
            notEmpty = value;
        }
    }

    // Class with default message
    private class NotEmptyClassDefaultMessage {
        @SuppressWarnings("unused")
        @NotEmpty
        String notEmpty;

        NotEmptyClassDefaultMessage(String value) {
            notEmpty = value;
        }
    }


    // Function to test class with own or default message
    private void checkNotEmptyClass(
                                    String  testString,
                                    boolean needsError,
                                    boolean ownMessage) {
        Set<ConstraintViolation<Object>> constraintViolations;
        Object                           notEmptyClass;
        String                           validationString;

        if (ownMessage) {
            notEmptyClass       = new NotEmptyClassOwnMessage(testString);
            validationString    = errorMessage;
        } else {
            notEmptyClass       = new NotEmptyClassDefaultMessage(testString);
            validationString    = NotEmpty.errorMessage;
        }
        constraintViolations = validator.validate(notEmptyClass);
        if (needsError) {
            assertEquals(constraintViolations.size(), 1);
            assertEquals(constraintViolations.iterator().next().getMessage(),
                         validationString);
        } else {
            assertTrue(constraintViolations.isEmpty());
        }
    }

    private final static String    errorMessage = "Field notEmpty should be filled";
    private final static String    filledString = "Filled string";
    private final static Validator validator    = Validation.buildDefaultValidatorFactory().getValidator();
}
