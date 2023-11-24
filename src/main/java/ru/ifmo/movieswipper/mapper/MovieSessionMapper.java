package ru.ifmo.movieswipper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.ifmo.movieswipper.dto.MovieSessionDTO;
import ru.ifmo.movieswipper.model.MovieSession;

@Mapper
public interface MovieSessionMapper {
    MovieSessionMapper INSTANCE = Mappers.getMapper(MovieSessionMapper.class);
    MovieSessionDTO toDomain(MovieSession movieSession);
}
