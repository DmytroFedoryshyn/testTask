package test.entity;

import java.time.LocalDate;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof User otherUser)) {
            return false;
        }

        return id.equals(otherUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
