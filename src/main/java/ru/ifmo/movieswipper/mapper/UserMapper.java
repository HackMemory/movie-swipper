package ru.ifmo.movieswipper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.ifmo.movieswipper.dto.UserDTO;
import ru.ifmo.movieswipper.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toDomain(User user);
}
