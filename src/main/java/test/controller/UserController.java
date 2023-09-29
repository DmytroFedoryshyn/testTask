package test.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import test.dto.DateRangeDto;
import test.dto.PartialUserRequestDto;
import test.dto.UserRequestDto;
import test.dto.UserResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.exception.AgeRestrictionException;
import test.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public UserResponseDto register(@Valid @RequestBody UserRequestDto dto) {
        return service.registerUser(dto);
    }

    @PutMapping("update/{id}")
    public UserResponseDto update(@PathVariable Long id, @Valid @RequestBody UserRequestDto dto) {
        return service.update(id, dto);
    }

    @PutMapping("partialUpdate/{id}")
    public UserResponseDto partialUpdate(@PathVariable Long id, @Valid @RequestBody PartialUserRequestDto dto) {
        return service.partialUpdate(id, dto);
    }

    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/searchByBirthDate")
    public List<UserResponseDto> searchByBirthDate(@Valid @RequestBody DateRangeDto dto) {
        return service.searchByBirthDate(dto);
    }

    @GetMapping
    public List<UserResponseDto> getAll() {
        return service.getAll();
    }
}
