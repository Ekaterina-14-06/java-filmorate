package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
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

import java.util.Set;
import java.util.ArrayList;

@Validated
@RestController
@Slf4j
// @RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;                  // для организации доступа к FilmService из FilmController
    private final UserService userService;                  // для организации доступа к UserService из FilmController
    private final InMemoryFilmStorage inMemoryFilmStorage;  // для организации доступа к inMemoryFilmStorage из FilmController
    private final InMemoryUserStorage inMemoryUserStorage;  // для организации доступа к inMemoryUserStorage из FilmController
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
        return inMemoryFilmStorage.createFilm(film);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @GetMapping(value = "/films/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    @GetMapping(value = "/films/popular?count={count}")
    public ArrayList<Film> getTopFilms(@PathVariable int count) {
        return filmService.getTopFilms(count);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping (value = "/films/genres/")
    public  Set<FilmGenre> getAllGenres () {
        return filmDbService.getGenres();
    }

    @GetMapping (value = "/films/genres/{id}")
    public  FilmGenre getGenre (@PathVariable Long id) {
        return filmDbService.getGenreById(id);
    }

    @GetMapping (value = "/films/mpa/")
    public  Set<FilmRating> getAllRatings () {
        return filmDbService.getRatings();
    }

    @GetMapping (value = "/films/mpa/{id}")
    public  FilmRating getRating (@PathVariable Long id) {
        return filmDbService.getRatingById(id);
    }
}
