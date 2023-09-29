package test.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import test.validation.ValidDateRange;

@Data
@ValidDateRange
public class DateRangeDto {
    @NotNull
    private LocalDate from;
    @NotNull
    private LocalDate to;
}
