@NotEmpty
=========

This is just an example how to write your own annotations in Java.

If something could be explained better:  
Let me know.

The class itself
----------------

With @NotEmpty you can make sure that a String field is not null and contains at least one character.

All three lines below are needed. Several sources were only talking about message, but the other two are also needed.

```
    Class<?>[]                  groups() default   { };
    String                      message() default errorMessage;
    Class<? extends Payload>[]  payload() default  {};
```

I use
```
    final static String         errorMessage =     "Field should be filled";
```

as default for message, so I can write my test cases in a DRY way.


Of-course you need to validate this also in your code and you need to test your class. This is explained below.

I added
```
@ReportAsSingleViolation
```

because I want no more as one violation. In this case it is not necessary, but I prefer to use it always. Lessens the chance I forget it when it is needed.


Testing the class
-----------------

You need to test your annotation of-course. A null or an empty String should give a constraint violation and when a String has at least one character, there should be no constraint violation.

I defined the following two classes:
```
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
```

to do my tests with the default message of the annotation and a user defined message.

I defined one function to test all the possibilities:
```
    // Function to test class with own or default message
    private void checkNotEmptyClass(
                                    String  testString,
                                    boolean needsError,
                                    boolean ownMessage) {
```

Because there are two different classes I use the super class object to hold the two different types and validationString to hold the expected message when a constraint violation is generated:
```
        Set<ConstraintViolation<Object>> constraintViolations;
        Object                           notEmptyClass;
        String                           validationString;
```

I use ownMessage to decide which class and message to use:
```
        if (ownMessage) {
            notEmptyClass       = new NotEmptyClassOwnMessage(testString);
            validationString    = errorMessage;
        } else {
            notEmptyClass       = new NotEmptyClassDefaultMessage(testString);
            validationString    = NotEmpty.errorMessage;
        }
```

I validate the created instance:
```
        constraintViolations = validator.validate(notEmptyClass);
```

When there should be an error, check that there is exactly one and that it has the correct message:
```
        if (needsError) {
            assertEquals(constraintViolations.size(), 1);
            assertEquals(constraintViolations.iterator().next().getMessage(),
                         validationString);
```

When there should not be an error, check that there is none:
```
        } else {
            assertTrue(constraintViolations.isEmpty());
        }
```

Maven Install script:
---------------------

There is also a pom.xml file for people that use Maven (3). For this I also included a Bash script NotEmpty.sh
