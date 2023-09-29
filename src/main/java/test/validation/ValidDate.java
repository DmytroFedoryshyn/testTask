package test.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
    String message() default "Date must be earlier than current date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
