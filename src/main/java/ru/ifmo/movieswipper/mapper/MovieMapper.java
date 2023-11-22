package ru.ifmo.movieswipper.mapper;

import info.movito.themoviedbapi.model.MovieDb;
import lombok.experimental.UtilityClass;
import ru.ifmo.movieswipper.model.Movie;
import ru.ifmo.movieswipper.model.Rate;
import ru.ifmo.movieswipper.util.DateTimeUtil;

@UtilityClass
public class MovieMapper {

    public static Movie toDomain(final MovieDb movieDb) {
        if (movieDb == null) {
            return null;
        }
        return Movie.builder()
                .id(String.valueOf(movieDb.getId()))
                .title(movieDb.getTitle())
                .synopsys(movieDb.getOverview())
                .rate(new Rate(movieDb.getPopularity()))
                .releaseDate(DateTimeUtil.getFromISO(movieDb.getReleaseDate()))
                .build();
    }
}