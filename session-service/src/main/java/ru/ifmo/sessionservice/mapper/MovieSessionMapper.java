package ru.ifmo.sessionservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ru.ifmo.sessionservice.dto.MovieSessionDTO;
import ru.ifmo.sessionservice.model.MovieSession;

@Mapper
public interface MovieSessionMapper {
    MovieSessionMapper INSTANCE = Mappers.getMapper(MovieSessionMapper.class);
    MovieSessionDTO toDomain(MovieSession movieSession);
}
