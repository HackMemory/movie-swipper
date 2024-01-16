package ru.ifmo.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ru.ifmo.authservice.dto.UserDTO;
import ru.ifmo.authservice.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toDomain(User user);
    User fromDomain(UserDTO user);
}
