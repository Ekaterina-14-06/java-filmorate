package ru.yandex.practicum.model.controller;

// import org.slf4j.Logger;         // Не пригодилось, поскольку используем import lombok.extern.slf4j.Slf4j для аннотации @Slf4j
// import org.slf4j.LoggerFactory;  // Не пригодилось, поскольку используем import lombok.extern.slf4j.Slf4j для аннотации @Slf4j
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import ru.yandex.practicum.model.model.Film;
import ru.yandex.practicum.model.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@Slf4j
public class FilmController {
    private final Set<Film> films = new HashSet<>();
    // создаём ЛОГЕР - Не понадобилось, поскольку используем аннотацию @Slf4j
    // private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    // ЭНДПОИНТ: получение списка всех фильмов
    @GetMapping("/films")
    public Set<Film> getAllFilms() {
        // Согласно ТЗ, необходимо добавить логирование только для операций, которые изменяют сущности — добавляют и обновляют их.
        // log.info("Количество фильмов {}", films.size());
        return films;
    }

    // ЭНДПОИНТ: добавление фильма
    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        try {
            for (Film filmInFilms : films) {
                if (filmInFilms.getId() == film.getId()) {
                    log.error("Ошибка при добавлении фильма с уже имеющимся id: {}",
                              film.getId());
                    throw new ValidationException("Фильм с таким id уже существует. " +
                                                  "Запись о фильме не была добавлена.");
                }
            }

            if (film.getDescription().length() > 200) {
                log.error("Попытка добавления фильм, длина описания которого превышает 200 символов: {}",
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
                log.error("Попытка добавить фильм, продолжительность которого отрицательная: {}",
                          film.getDuration());
                throw new ValidationException("Продолжительность фильма должна быть положительной. " +
                                              "Запись о фильме не была добавлена.");
            }

            films.add(film);
            log.info("Добавлен фильм {}", film.toString());
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return film;  // возвращение сущности - согласно ТЗ
    }

    // ЭНДПОИНТ: обновление фильма
    @PutMapping(value = "/films")  // или PatchMapping?
    public Film updateFilm(@Valid @RequestBody Film film) {
        try {
            boolean isPresent = false;
            for (Film filmInFilms : films) {
                if (filmInFilms.getId() == film.getId()) {
                    isPresent = true;

                    if (film.getDescription().length() > 200) {
                        log.error("Попытка добавления фильм, длина описания которого превышает 200 символов: {}",
                                  film.getDescription().length());
                        throw new ValidationException("Длина строки, содержащей описание фильма, не может превышать 200 символов. " +
                                                      "Запись о фильме не была добавлена.");
                    }

                    if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
                        log.error("Попытка добавления фильма, дата релиза которого ранше 28.12.1895 г.: {}",
                                  film.getReleaseDate());
                        throw new ValidationException("Дата релиза фильма не может быть раньше 28.12.1895 г. " +
                                                      "Запись о фильме не была добавлена.");
                    }

                    if (film.getDuration().isNegative()) {
                        log.error("Попытка добавить фильм, продолжительность которого отрицательная: {}",
                                  film.getDuration());
                        throw new ValidationException("Продолжительность фильма должна быть положительной. " +
                                                      "Запись о фильме не была добавлена.");
                    }

                    filmInFilms.setName(film.getName());
                    filmInFilms.setDescription(film.getDescription());
                    filmInFilms.setReleaseDate(film.getReleaseDate());
                    filmInFilms.setDuration(film.getDuration());

                    log.info("Обновлён фильм {}", film.toString());
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

        return film;  // возвращение сущности - согласно ТЗ
    }
}
