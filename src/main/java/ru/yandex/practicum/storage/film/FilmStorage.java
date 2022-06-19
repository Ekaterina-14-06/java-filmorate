// В пакете storage только классы и интерфейсы, имеющие отношение к хранению данных.
package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.model.Film;
import java.util.Set;

// Согласно ТЗ спринта 9 интерфейс FilmStorage определяет методы добавления, удаления и модификации объектов (фильмов).
public interface FilmStorage {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    Film getFilmById(Long filmId);
    Set<Film> getFilms();
}
