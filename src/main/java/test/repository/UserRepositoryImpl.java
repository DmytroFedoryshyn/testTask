package test.repository;

import test.entity.User;
import test.exception.DataProcessingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private Long idCount = 1L;
    private final List<User> users = new ArrayList<>();

    @Override
    public User save(User user) {
        user.setId(idCount++);
        users.add(user);
        return user;
    }

    @Override
    public User update(Long id, User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                user.setId(id);
                users.set(i, user);
                return user;
            }
        }

        throw new DataProcessingException("User with id " + id + " not found");
    }

    @Override
    public User partialUpdate(Long id, User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                User toUpdate = users.get(i);

                if (user.getEmail() != null) {
                    toUpdate.setEmail(user.getEmail());
                }

                if (user.getBirthDate() != null) {
                    toUpdate.setBirthDate(user.getBirthDate());
                }

                if (user.getAddress() != null) {
                    toUpdate.setAddress(user.getAddress());
                }

                if (user.getFirstName() != null) {
                    toUpdate.setFirstName(user.getFirstName());
                }

                if (user.getLastName() != null) {
                    toUpdate.setLastName(user.getLastName());
                }

                if (user.getPhoneNumber() != null) {
                    toUpdate.setPhoneNumber(user.getPhoneNumber());
                }

                users.set(i, toUpdate);
                return toUpdate;
            }
        }


        throw new DataProcessingException("User with id " + id + " not found");
    }

    @Override
    public boolean delete(Long id) {
        int index;
        for (index = 0; index < users.size(); index++) {
            if (users.get(index).getId().equals(id)) {
                users.remove(index);
               return true;
            }
        }

        return false;
    }

    @Override
    public List<User> searchByBirthDate(LocalDate from, LocalDate to) {
        return users.stream()
            .filter(user -> user.getBirthDate().isBefore(to)
            && user.getBirthDate().isAfter(from))
            .collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return users;
    }

    @Override
    public void deleteAll() {
        users.clear();
    }
}
