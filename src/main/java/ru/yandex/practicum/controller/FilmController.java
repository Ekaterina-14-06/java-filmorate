package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.FilmGenre;
import ru.yandex.practicum.model.FilmRating;
import ru.yandex.practicum.service.FilmDbService;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Validated
@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;
    private final FilmDbService filmDbService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService,
                          InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage, FilmDbService filmDbService) {
        this.filmService = filmService;
        this.userService = userService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.filmDbService = filmDbService;
    }

    @GetMapping("/films")
    public Set<Film> getAllFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);
    }

    @GetMapping(value = "/films/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    @GetMapping(value = "/films/popular?count={count}")
    public List<Film> getTopFilms(@PathVariable int count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping(value = "/films/popularDb?count={count}")
    public Map<Long, Integer> getTopFilmsFromDb(@PathVariable int count) {
        return filmDbService.getTopFilms(count);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        filmDbService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
        filmDbService.deleteLike(id, userId);
    }

    @GetMapping(value = "/films/genres/")
    public Set<FilmGenre> getAllGenres() {
        return filmDbService.getGenres();
    }

    @GetMapping(value = "/films/genres/{id}")
    public FilmGenre getGenre(@PathVariable Long id) {
        return filmDbService.getGenreById(id);
    }

    @GetMapping(value = "/films/mpa/")
    public Set<FilmRating> getAllRatings() {
        return filmDbService.getRatings();
    }

    @GetMapping(value = "/films/mpa/{id}")
    public FilmRating getRating(@PathVariable Long id) {
        return filmDbService.getRatingById(id);
    }

    @GetMapping(value = "/films/{id}/like/")
    public Set<Long> getLikesByFilmId(@PathVariable Long id) {
        return filmDbService.getLikesByFilmId(id);
    }
}
