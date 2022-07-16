package ru.yandex.practicum.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dao.FilmDao;
import ru.yandex.practicum.model.Film;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmDbStorage implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("INSERT INTO films (name,  description, release_date, duration, id_rating) " +
                        "VALUES (?, ?, ?, ?, ?)",
                film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getRatingId());

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT id FROM films WHERE name = ?", film.getName());
        filmRows.next();
        Long tempId = filmRows.getLong("id");
        film.setId(tempId);

        for (Film.Likes l : film.getLikesDb()) {
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("INSERT INTO likes (id_film, id_user) VALUES (?, ?)",
                    tempId, l.getUserId());
        }

        for (Film.Genres g : film.getGenresDb()) {
            SqlRowSet genresRows = jdbcTemplate.queryForRowSet("INSERT INTO film_genres (id_film, id_genre) VALUES (?, ?)",
                    tempId, g.getGenreId());
        }

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, id_rating " +
                "WHERE id = ?", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRatingId(), film.getId());

        for (Film.Likes l : film.getLikesDb()) {
            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("UPDATE likes SET id_user = ? WHERE id_film = ?",
                    l.getUserId(), film.getId());
        }

        for (Film.Genres g : film.getGenresDb()) {
            SqlRowSet genresRows = jdbcTemplate.queryForRowSet("UPDATE film_genres SET id_genre = ? WHERE id_film = ?",
                    g.getGenreId(), film.getId());
        }
        return film;
    }

    @Override
    public Set<Film> getFilms() {
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT * FROM films");
        Set<Film> films = new HashSet<>();
        while (filmsRows.next()) {
            Film film = new Film();
            film.setId(filmsRows.getLong("id"));
            film.setName(filmsRows.getString("name"));
            film.setDescription(filmsRows.getString("description"));
            film.setReleaseDate(filmsRows.getDate("release_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            film.setDuration(filmsRows.getInt("duration"));
            film.setRatingId(filmsRows.getLong("id_rating"));

            SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT * FROM film_genre WHERE id_film = ?", film.getId());
            while (genresRows.next()) {
                Film.Genres genre = new Film.Genres(film.getId(), genresRows.getLong("id_genre"));
                film.getGenresDb().add(genre);
            }

            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE id_film = ?", film.getId());
            while (likesRows.next()) {
                Film.Likes like = new Film.Likes(film.getId(), likesRows.getLong("id_user"));
                film.getLikesDb().add(like);
            }

            films.add(film);
        }
        return films;
    }

    @Override
    public Film getFilmById(Long filmId) {
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE id = ?", filmId);
        if (filmsRows.next()) {
            Film film = new Film();
            film.setId(filmsRows.getLong("id"));
            film.setName(filmsRows.getString("name"));
            film.setDescription(filmsRows.getString("description"));
            film.setReleaseDate(filmsRows.getDate("release_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            film.setDuration(filmsRows.getInt("duration"));
            film.setRatingId(filmsRows.getLong("id_rating"));

            SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT * FROM film_genre WHERE id_film = ?", filmId);
            while (genresRows.next()) {
                Film.Genres genre = new Film.Genres(filmId, genresRows.getLong("id_genre"));
                film.getGenresDb().add(genre);
            }

            SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes WHERE id_film = ?", filmId);
            while (likesRows.next()) {
                Film.Likes like = new Film.Likes(filmId, likesRows.getLong("id_user"));
                film.getLikesDb().add(like);
            }

            return film;
        } else {
            return null;
        }
    }

    @Override
    public void deleteFilms() {
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("DELETE * FROM films");
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("DELETE * FROM film_genres");
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("DELETE * FROM likes");
    }

    @Override
    public void deleteFilmById(Long filmId) {
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("DELETE * FROM film_genres WHERE id = ?", filmId);
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("DELETE * FROM likes WHERE id = ?", filmId);
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("DELETE * FROM films WHERE id = ?", filmId);
    }
}