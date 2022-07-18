package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;

import java.util.Set;

public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long filmId);

    Set<Film> getFilms();

    void deleteFilm(Film film);

    void deleteFilmById(Long filmId);
}
