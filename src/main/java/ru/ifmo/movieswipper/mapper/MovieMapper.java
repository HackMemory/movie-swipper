package ru.ifmo.movieswipper.mapper;

import info.movito.themoviedbapi.model.MovieDb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import ru.ifmo.movieswipper.dto.MovieDTO;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mappings({
            @Mapping(target = "synopsys", source = "overview"),
            @Mapping(target = "rate", source = "voteAverage")
    })
    MovieDTO toDomain(MovieDb movie);
}