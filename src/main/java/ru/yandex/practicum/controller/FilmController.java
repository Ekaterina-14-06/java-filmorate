package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import java.util.Set;
import java.util.ArrayList;

@Validated
@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;                  // для организации доступа к FilmService из FilmController
    private final UserService userService;                  // для организации доступа к UserService из FilmController
    private final InMemoryFilmStorage inMemoryFilmStorage;  // для организации доступа к inMemoryFilmStorage из FilmController
    private final InMemoryUserStorage inMemoryUserStorage;  // для организации доступа к inMemoryUserStorage из FilmController

    @Autowired  // Внедрение зависимостей.
    public FilmController(FilmService filmService,
                          UserService userService,
                          InMemoryFilmStorage inMemoryFilmStorage,
                          InMemoryUserStorage inMemoryUserStorage) {
        this.filmService = filmService;
        this.userService = userService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // ЭНДПОИНТ: получение списка всех фильмов (согласно ТЗ спринта 8)
    @GetMapping("/films")
    public Set<Film> getAllFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    // ЭНДПОИНТ: добавление фильма (согласно ТЗ спринта 8)
    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.createFilm(film);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    // ЭНДПОИНТ: обновление фильма (согласно ТЗ спринта 8)
    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return inMemoryFilmStorage.updateFilm(film);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    // ЭНДПОИНТ: получение фильма по уникальному идентификатору (согласно ТЗ спринта 9)
    // GET /films/{id}
    @GetMapping(value = "/films/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return inMemoryFilmStorage.getFilmById(id);
    }

    // ЭНДПОИНТ: возвращение списка из первых count фильмов по количеству лайков
    // (если значение параметра count не задано, то вернуть первые 10 фильмов) (согласно ТЗ спринта 9)
    // GET /films/popular?count={count}
    @GetMapping(value = "/films/popular?count={count}")
    public ArrayList<Film> getTopFilms(@PathVariable int count) {
        return filmService.getTopFilms(count);
    }

    // ЭНДПОИНТ: пользователь ставит лайк фильму (согласно ТЗ спринта 9)
    // PUT /films/{id}/like/{userId}
    @PutMapping(value = "/films/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    // ЭНДПОИНТ: пользователь удаляет лайк у фильма (согласно ТЗ спринта 9)
    // DELETE /films/{id}/like/{userId}
    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public void deleteLikeFromFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.deleteLike(id, userId);
    }
}
