package ru.ifmo.movieservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import info.movito.themoviedbapi.model.MovieDb;
import ru.ifmo.movieservice.dto.MovieDTO;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mappings({
            @Mapping(target = "synopsys", source = "overview"),
            @Mapping(target = "rate", source = "voteAverage")
    })
    MovieDTO toDomain(MovieDb movie);
}