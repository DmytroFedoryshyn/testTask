package test.service;

import test.dto.DateRangeDto;
import test.dto.PartialUserRequestDto;
import test.dto.UserRequestDto;
import test.dto.UserResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto registerUser(UserRequestDto user);

    UserResponseDto update(Long id, UserRequestDto dto);

    UserResponseDto partialUpdate(Long id, PartialUserRequestDto user);

    boolean delete(Long id);

    List<UserResponseDto> searchByBirthDate(DateRangeDto dto);

    List<UserResponseDto> getAll();
}
