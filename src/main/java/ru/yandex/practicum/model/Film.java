package ru.yandex.practicum.model;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Data
public class Film {
    private Long id;
    @NotEmpty
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likes;
    private Set<Long> genres;
    private Long ratingId;
    private Set<Likes> likesDb;
    private Set<Genres> genresDb;

    @Data
    public static class Likes {
        private Long filmId;
        private Long userId;

        public Likes(Long filmId, Long userId) {
            this.filmId = filmId;
            this.userId = userId;
        }
    }

    @Data
    public static class Genres {
        private Long filmId;
        private Long genreId;

        public Genres(Long filmId, Long genreId) {
            this.filmId = filmId;
            this.genreId = genreId;
        }
    }
}
