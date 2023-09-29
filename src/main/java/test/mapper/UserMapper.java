package test.mapper;

import test.config.MapperConfig;
import test.dto.PartialUserRequestDto;
import test.dto.UserRequestDto;
import test.dto.UserResponseDto;
import test.entity.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toEntity(UserRequestDto dto);

    User toEntity(PartialUserRequestDto dto);
}
