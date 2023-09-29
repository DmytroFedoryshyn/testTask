package test.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import test.validation.ValidDate;

@Data
public class UserRequestDto {
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @ValidDate
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
}
