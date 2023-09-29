package test.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateRange {
    String message() default "Invalid date range: from date must be earlier than to date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
