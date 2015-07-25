package nl.decebal.validator;


import        java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import        java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import        java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.ReportAsSingleViolation;


// public
@Documented
@Constraint(validatedBy = {})
@Target({ FIELD })
@Retention(RUNTIME)
@ReportAsSingleViolation
@NotNull
@Size(min = 1)
public @interface NotEmpty {
    // default
    final static String         errorMessage =     "Field should be filled";
    Class<?>[]                  groups() default   { };
    String                      message() default errorMessage;
    Class<? extends Payload>[]  payload() default  {};
}
