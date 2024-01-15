package ru.ifmo.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ru.ifmo.userservice.dto.UserDTO;
import ru.ifmo.userservice.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toDomain(User user);
}
