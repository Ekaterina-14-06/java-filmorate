// В пакете storage хранятся только классы и интерфейсы, имеющие отношение к хранению данных.
package ru.yandex.practicum.storage.film;

import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component  // Аннотация @Component - для внедрения зависимостей и передачи хранилища сервисам.
// Согласно ТЗ спринта 9 класс InMemoryFilmStorage содержит логику хранения, обновления и поиска объектов (фильмов).
public class InMemoryFilmStorage implements FilmStorage {

    // Поле films - список фильмов.
    private final Set<Film> films = new HashSet<>();

    // Поле countOfFilms - счётчик (определяет значение поля id).
    private Long countOfFilms = 0L;

    @Override
    public Film createFilm(Film film) {
        try {
            if (film.getDescription().length() > 200) {
                log.error("Попытка добавления фильма, длина описания которого превышает 200 символов: {}",
                        film.getDescription().length());
                throw new ValidationException("Длина строки, содержащей описание фильма, не может превышать 200 символов. " +
                        "Запись о фильме не была добавлена.");
            }

            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                log.error("Попытка добавления фильма, дата релиза которого раньше 28.12.1895 г.: {}",
                        film.getReleaseDate());
                throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895 г. " +
                        "Запись о фильме не была добавлена.");
            }

            if (film.getDuration().isNegative()) {
                log.error("Попытка добавления фильма, продолжительность которого отрицательная: {}",
                        film.getDuration());
                throw new ValidationException("Продолжительность фильма должна быть положительной. " +
                        "Запись о фильме не была добавлена.");
            }

            film.setId(++countOfFilms);
            films.add(film);
            log.info("Добавлен фильм {}", film);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return film;  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @Override
    public Film updateFilm(Film film) {
        try {
            boolean isPresent = false;
            for (Film filmInFilms : films) {
                if (filmInFilms.getId() == film.getId()) {
                    isPresent = true;

                    if (film.getDescription().length() > 200) {
                        log.error("Попытка добавления фильма, длина описания которого превышает 200 символов: {}",
                                film.getDescription().length());
                        throw new ValidationException("Длина строки, содержащей описание фильма, не может превышать 200 символов. " +
                                "Запись о фильме не была добавлена.");
                    }

                    if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                        log.error("Попытка добавления фильма, дата релиза которого раньше 28.12.1895 г.: {}",
                                film.getReleaseDate());
                        throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895 г. " +
                                "Запись о фильме не была добавлена.");
                    }

                    if (film.getDuration().isNegative()) {
                        log.error("Попытка добавления фильма, продолжительность которого отрицательная: {}",
                                film.getDuration());
                        throw new ValidationException("Продолжительность фильма должна быть положительной. " +
                                "Запись о фильме не была добавлена.");
                    }

                    filmInFilms.setName(film.getName());
                    filmInFilms.setDescription(film.getDescription());
                    filmInFilms.setReleaseDate(film.getReleaseDate());
                    filmInFilms.setDuration(film.getDuration());
                    filmInFilms.setLikes(film.getLikes());

                    log.info("Обновлён фильм {}", film);
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменить свойства несуществующего фильма (нет совпадений по id {}).",
                        film.getId());
                throw new ValidationException("Фильма с таким id не существует (нечего обновлять). " +
                        "Запись о фильме не была обновлена.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return film;  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @Override
    // Получение всех фильмов
    public Set<Film> getFilms() {
        return films;
    }

    @Override
    // Получение фильма по его id
    public Film getFilmById(Long filmId) {
        try {
            for (Film film : films) {
                if (film.getId() == filmId) {
                    return film;
                }
            }

            log.error("Попытка получения несуществующего фильма (нет совпадений по id ).");
            throw new ValidationException("Фильм с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteFilm(Film film) {
        try {
            if (!films.contains(film)) {
                log.error("Попытка удаления несуществующего фильма: {}", film);
                throw new ValidationException("Попытка удаления несуществующего фильма.");
            }

            films.remove(film);
            log.info("Удалён фильм {}", film);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteFilmById(Long filmId) {
        Film film = getFilmById(filmId);

        try {
            if (!films.contains(film)) {
                log.error("Попытка удаления несуществующего фильма: {}", film);
                throw new ValidationException("Попытка удаления несуществующего фильма.");
            }

            films.remove(film);
            log.info("Удалён фильм {}", film);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }


}
