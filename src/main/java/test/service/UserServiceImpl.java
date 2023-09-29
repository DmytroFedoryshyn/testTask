package test.service;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.core.env.Environment;
import test.dto.DateRangeDto;
import test.dto.PartialUserRequestDto;
import test.dto.UserRequestDto;
import test.dto.UserResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import test.exception.AgeRestrictionException;
import test.mapper.UserMapper;
import org.springframework.stereotype.Service;
import test.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final Environment environment;

    @Override
    public UserResponseDto registerUser(UserRequestDto user) {
        int minAge = Integer.parseInt(environment.getProperty("user.registration.min-age"));

        LocalDate birthDate = user.getBirthDate();
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);

        if (age.getYears() < minAge) {
            throw new AgeRestrictionException("User must be at least 18 years old.");
        }
        return mapper.toDto(repository.save(mapper.toEntity(user)));
    }

    @Override
    public UserResponseDto update(Long id, UserRequestDto dto) {
        return mapper.toDto(repository.update(id, mapper.toEntity(dto)));
    }

    @Override
    public UserResponseDto partialUpdate(Long id, PartialUserRequestDto user) {
        return mapper.toDto(repository.partialUpdate(id, mapper.toEntity(user)));
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public List<UserResponseDto> searchByBirthDate(DateRangeDto dto) {
        return repository.searchByBirthDate(dto.getFrom(), dto.getTo())
            .stream()
            .map(mapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getAll() {
        return repository.getAll().stream()
            .map(mapper::toDto).collect(Collectors.toList());
    }
}
