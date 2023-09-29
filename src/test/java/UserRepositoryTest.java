import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import test.Application;
import test.entity.User;
import test.repository.UserRepository;

@SpringBootTest(classes = Application.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User savedUser = userRepository.save(getSampleUser());

        assertNotNull(savedUser.getId());
    }

    @Test
    public void testUpdateUser() {
        User saved = userRepository.save(getSampleUser());

        User updatedUser = new User();
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");
        updatedUser.setEmail("jane@example.com");
        updatedUser.setBirthDate(LocalDate.of(1985, 5, 10));

        User result = userRepository.update(saved.getId(), updatedUser);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("jane@example.com", result.getEmail());
    }

    @Test
    public void testPartialUpdateUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 15));
        userRepository.save(user);

        User partialUpdate = new User();
        partialUpdate.setEmail("jane@example.com");

        User result = userRepository.partialUpdate(user.getId(), partialUpdate);

        assertNotNull(result);
        assertEquals("jane@example.com", result.getEmail());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    public void testDeleteUser() {
        User saved = userRepository.save(getSampleUser());

        boolean deleted = userRepository.delete(saved.getId());

        assertTrue(deleted);

        assertTrue(userRepository.getAll().isEmpty());
    }

    @Test
    public void testSearchByBirthDate() {
        User user1 = new User();
        user1.setFirstName("User1");
        user1.setLastName("Last1");
        user1.setEmail("user1@example.com");
        user1.setBirthDate(LocalDate.of(1980, 1, 1));
        userRepository.save(user1);

        User user2 = new User();
        user2.setFirstName("User2");
        user2.setLastName("Last2");
        user2.setEmail("user2@example.com");
        user2.setBirthDate(LocalDate.of(1990, 1, 1));
        userRepository.save(user2);

        User user3 = new User();
        user3.setFirstName("User3");
        user3.setLastName("Last3");
        user3.setEmail("user3@example.com");
        user3.setBirthDate(LocalDate.of(2000, 1, 1));
        userRepository.save(user3);

        LocalDate fromDate = LocalDate.of(1985, 1, 1);
        LocalDate toDate = LocalDate.of(1995, 1, 1);

        List<User> usersInDateRange = userRepository.searchByBirthDate(fromDate, toDate);

        assertNotNull(usersInDateRange);
        assertEquals(1, usersInDateRange.size());
        assertTrue(usersInDateRange.contains(user2));
        assertFalse(usersInDateRange.contains(user1));
        assertFalse(usersInDateRange.contains(user3));
    }

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
    }

    private User getSampleUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 15));

        return user;
    }
}
