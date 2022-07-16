package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Film;

import java.util.Set;

public interface FilmDao {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    Film getFilmById(Long filmId);
    Set<Film> getFilms();
    void deleteFilms();
    void deleteFilmById(Long filmId);
}
