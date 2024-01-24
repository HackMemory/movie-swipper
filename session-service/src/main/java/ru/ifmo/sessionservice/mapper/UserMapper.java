package ru.ifmo.sessionservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ru.ifmo.sessionservice.dto.UserDTO;
import ru.ifmo.sessionservice.model.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toDomain(User user);
    User fromDomain(UserDTO userDTO);
}
