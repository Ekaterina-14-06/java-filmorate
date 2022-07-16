package ru.yandex.practicum.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.model.FilmRating;

import java.util.*;

public class FilmDbService {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.queryForRowSet("INSERT INTO likes (id_user, id_film) VALUES (?, ?)", userId, filmId);
    }

    public Set<Long> getLikesByFilmId(Long filmId) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT id_user FROM likes WHERE id_film = ?", filmId);

        Set<Long> userIds = new HashSet<>();
        while (likesRows.next()) {
            userIds.add(likesRows.getLong("id_user"));
        }
        return userIds;
    }

    public Set<Long> getLikeByUserId(Long userId) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT id_film FROM likes WHERE id_user = ?", userId);

        Set<Long> filmIds = new HashSet<>();
        while (likesRows.next()) {
            filmIds.add(likesRows.getLong("id_film"));
        }
        return filmIds;
    }

    public void deleteLike(Long filmId, Long userId) {
        jdbcTemplate.queryForRowSet("DELETE * FROM likes WHERE id_film = ? AND id_user = ?", filmId, userId);
    }

    public Map<Long, Integer> getTopFilms(Integer sizeOfList) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT films.id, COUNT(likes.*) AS count_of_likes " +
                "FROM films LEFT INNER JOIN likes " +
                "ON films.id = likes.id_film " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(likes.*) DESC TOP ?", sizeOfList);

        Map<Long, Integer> filmIds = new HashMap<>();
        while (likesRows.next()) {
            filmIds.put(likesRows.getLong("id"), likesRows.getInt("count_of_likes"));
        }
        return filmIds;
    }

    public Set<FilmGenre> getGenres() {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        Set<FilmGenre> genres = new HashSet<>();
        while (genreRows.next()) {
            FilmGenre filmGenre = new FilmGenre(genreRows.getLong("id"),
                    genreRows.getString("name"));
            genres.add(filmGenre);
        }
        return genres;
    }

    public FilmGenre getGenreById(Long genreId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE id = ?", genreId);
        if (genreRows.next()) {
            FilmGenre fg = new FilmGenre(genreRows.getLong("id"), genreRows.getString("name"));
            return fg;
        } else {
            return null;
        }
    }

    public Set<FilmRating> getRatings() {
        SqlRowSet ratingsRows = jdbcTemplate.queryForRowSet("SELECT * FROM ratings");
        Set<FilmRating> ratings = new HashSet<>();
        while (ratingsRows.next()) {
            FilmRating filmRating = new FilmRating(ratingsRows.getLong("id"),
                    ratingsRows.getString("name"));
            ratings.add(filmRating);
        }
        return ratings;
    }

    public FilmRating getRatingById(Long ratingId) {
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet("SELECT * FROM ratings WHERE id = ?", ratingId);
        if (ratingRows.next()) {
            FilmRating fr = new FilmRating(ratingRows.getLong("id"),
                    ratingRows.getString("name"));
            return fr;
        } else {
            return null;
        }
    }
}
