import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import test.Application;
import test.dto.DateRangeDto;
import test.dto.UserRequestDto;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ValidationTest {
    @Autowired
    private Validator validator;

    @Test
    public void testValidDate() {
        UserRequestDto dto = new UserRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("email@example.com");
        dto.setBirthDate(LocalDate.now().minusYears(20));
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidDate() {
        UserRequestDto dto = new UserRequestDto();
        dto.setBirthDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidDateRange() {
        DateRangeDto dto = new DateRangeDto();
        dto.setFrom(LocalDate.now().minusDays(20));
        dto.setTo(LocalDate.now());
        Set<ConstraintViolation<DateRangeDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidDateRange() {
        DateRangeDto dto = new DateRangeDto();
        dto.setFrom(LocalDate.now().plusDays(20));
        dto.setTo(LocalDate.now());
        Set<ConstraintViolation<DateRangeDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
