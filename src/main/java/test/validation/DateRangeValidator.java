package test.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import test.dto.DateRangeDto;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRangeDto> {
    @Override
    public boolean isValid(DateRangeDto dateRangeDto, ConstraintValidatorContext context) {
        if (dateRangeDto == null) {
            return true;
        }

        LocalDate from = dateRangeDto.getFrom();
        LocalDate to = dateRangeDto.getTo();

        return from.isBefore(to);
    }
}
