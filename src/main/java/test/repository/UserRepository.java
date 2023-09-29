package test.repository;

import test.entity.User;
import java.time.LocalDate;
import java.util.List;

public interface UserRepository {
    User save(User user);

    User update(Long id, User user);

    User partialUpdate(Long id, User user);

    boolean delete(Long id);

    List<User> searchByBirthDate(LocalDate from, LocalDate to);

    List<User> getAll();

    void deleteAll();
}
