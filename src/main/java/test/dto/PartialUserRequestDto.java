package test.dto;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Data;
import test.validation.ValidDate;

@Data
public class PartialUserRequestDto {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    @ValidDate
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
