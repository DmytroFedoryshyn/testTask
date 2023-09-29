import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.Application;
import test.dto.DateRangeDto;
import test.dto.PartialUserRequestDto;
import test.dto.UserRequestDto;
import test.dto.UserResponseDto;
import test.exception.RegistrationException;
import test.repository.UserRepository;
import test.service.UserService;

@SpringBootTest(classes = Application.class)
public class UserServiceTest {
    @Autowired
    private UserRepository repository;

    @AfterEach
    public void clearRepository() {
        repository.deleteAll();
    }

    @Autowired
    private UserService userService;

    @Test
    public void testRegisterUserValid() {
        UserRequestDto dto = new UserRequestDto();
        dto.setBirthDate(LocalDate.now().minusYears(20));

        UserResponseDto response = userService.registerUser(dto);

        assertNotNull(response);
        assertNotNull(response.getId());
    }

    @Test
    public void testRegisterUserInvalid() {
        UserRequestDto dto = new UserRequestDto();
        dto.setBirthDate(LocalDate.now().plusDays(1));

        assertThrows(RegistrationException.class, () -> userService.registerUser(dto));
    }

    @Test
    public void testUpdateUser() {
        UserResponseDto createdUser = userService.registerUser(getSampleUser());

        UserRequestDto updateUserDto = new UserRequestDto();
        updateUserDto.setEmail("jane@example.com");

        UserResponseDto updatedUser = userService.update(createdUser.getId(), updateUserDto);

        assertNotNull(updatedUser);
        assertEquals("jane@example.com", updatedUser.getEmail());
    }

    @Test
    public void testPartialUpdateUser() {
        UserResponseDto createdUser = userService.registerUser(getSampleUser());

        PartialUserRequestDto partialUpdateDto = new PartialUserRequestDto();
        partialUpdateDto.setEmail("jane@example.com");
        partialUpdateDto.setAddress("123 Main St");

        UserResponseDto updatedUser = userService.partialUpdate(createdUser.getId(), partialUpdateDto);

        assertNotNull(updatedUser);
        assertEquals("jane@example.com", updatedUser.getEmail());
        assertEquals("123 Main St", updatedUser.getAddress());
    }

    @Test
    public void testDeleteUser() {
        UserResponseDto createdUser = userService.registerUser(getSampleUser());

        userService.delete(createdUser.getId());

        List<UserResponseDto> users = userService.getAll();
        assertTrue(users.isEmpty());
    }

    @Test
    public void testSearchByBirthDate() {
        UserRequestDto user1Dto = new UserRequestDto();
        user1Dto.setFirstName("User1");
        user1Dto.setLastName("Last1");
        user1Dto.setEmail("user1@example.com");
        user1Dto.setBirthDate(LocalDate.now().minusYears(30));
        UserResponseDto user1 = userService.registerUser(user1Dto);

        UserRequestDto user2Dto = new UserRequestDto();
        user2Dto.setFirstName("User2");
        user2Dto.setLastName("Last2");
        user2Dto.setEmail("user2@example.com");
        user2Dto.setBirthDate(LocalDate.now().minusYears(25));
        UserResponseDto user2 = userService.registerUser(user2Dto);

        UserRequestDto user3Dto = new UserRequestDto();
        user3Dto.setFirstName("User3");
        user3Dto.setLastName("Last3");
        user3Dto.setEmail("user3@example.com");
        user3Dto.setBirthDate(LocalDate.now().minusYears(20));
        UserResponseDto user3 = userService.registerUser(user3Dto);

        LocalDate fromDate = LocalDate.now().minusYears(29);
        LocalDate toDate = LocalDate.now().minusYears(21);

        DateRangeDto dateRange = new DateRangeDto();
        dateRange.setFrom(fromDate);
        dateRange.setTo(toDate);

        List<UserResponseDto> usersInDateRange = userService.searchByBirthDate(dateRange);

        assertNotNull(usersInDateRange);
        assertEquals(1, usersInDateRange.size());
        assertFalse(usersInDateRange.contains(user1));
        assertTrue(usersInDateRange.contains(user2));
        assertFalse(usersInDateRange.contains(user3));
    }

    private UserRequestDto getSampleUser() {
        UserRequestDto createUserDto = new UserRequestDto();
        createUserDto.setFirstName("John");
        createUserDto.setLastName("Doe");
        createUserDto.setEmail("john@example.com");
        createUserDto.setBirthDate(LocalDate.now().minusYears(25));
        return createUserDto;
    }
}
