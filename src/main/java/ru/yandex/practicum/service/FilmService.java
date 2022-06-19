package ru.yandex.practicum.service;  // Пакет service объединяет бизнес-логику.

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.film.FilmStorage;
import ru.yandex.practicum.storage.user.UserStorage;
import java.util.ArrayList;

@Slf4j
@Service  // Аннотация @Service - для получения доступа к этому классу FilmService из контроллера FilmController.
public class FilmService {
// Согласно ТЗ спринта 9 класс FilmService отвечает за следующие операции с фильмами:
// - добавление лайка фильму (каждый пользователь может поставить лайк фильму только один раз);
// - удаление лайка у фильма;
// - вывод 10 наиболее популярных фильмов по количеству лайков.

    private final FilmStorage inMemoryFilmStorage;  // для организации доступа к inMemoryFilmStorage из FilmService
    private final UserStorage inMemoryUserStorage;  // для организации доступа к inMemoryUserStorage из FilmService

    @Autowired  // Внедрение зависимостей.
    public FilmService(FilmStorage inMemoryFilmStorage,
                       UserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // Метод addLike добавляет лайк фильму.
    public void addLike(Long filmId, Long userId) {
        try {
            Film film = inMemoryFilmStorage.getFilmById(filmId);
            User user = inMemoryUserStorage.getUserById(userId);

            if (!film.getLikes().contains(user.getId())) {
                film.getLikes().add(user.getId());
            } else {
                log.error("Попытка повторного добавления лайка фильму одним пользователем.");
                throw new ValidationException("Этому фильму уже поставлен лайк данным пользователем. " +
                                              "Повторная установка лайка не произведена.");
            }
        } catch (ValidationException e) {
                System.out.println(e.getMessage());
        }
    }

    // Метод deleteLike удаляет лайк у фильма.
    public void deleteLike(Long filmId, Long userId) {
        try {
            Film film = inMemoryFilmStorage.getFilmById(filmId);
            User user = inMemoryUserStorage.getUserById(userId);

            if (film.getLikes().contains(user.getId())) {
                film.getLikes().remove(user.getId());
            } else {
                log.error("Попытка удаления лайка у фильма от пользователя, который этот лайк не ставил.");
                throw new ValidationException("Этому фильму данный пользователь лайк не ставил. " +
                                              "Удаление лайка не произведено.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод getTopFilms возвращает sizeOfList (или 10, если не указано значение поля sizeOfList)
    // наиболее популярных фильмов по количеству лайков.
    public ArrayList<Film> getTopFilms(@Autowired(required = false) Integer sizeOfList) {
        if (sizeOfList == null) {
            sizeOfList = 10;
        }


        // Вместо приведённого ниже алгоритма нужно попробовать использовать comparator...

        // Перевод списка из формата Set в формат ArrayList для получения возможности запоминать порядок элементов в списке:
        ArrayList<Film> sortedList = new ArrayList<>();
        sortedList.addAll(inMemoryFilmStorage.getFilms());

        // Сортируем элементы нового списка по уменьшению количества лайков:
        int size = sortedList.size();
        for (int j = 1; j < (size - 1); j++) {
            for (int i = 0; i < (size - j); i++) {
                if (sortedList.get(i).getLikes().size() < sortedList.get(i + 1).getLikes().size()) {
                    // Меняем местами элементы:
                    Film temp = sortedList.get(i);

                    sortedList.get(i).setId(sortedList.get(i + 1).getId());
                    sortedList.get(i).setName(sortedList.get(i + 1).getName());
                    sortedList.get(i).setDescription(sortedList.get(i + 1).getDescription());
                    sortedList.get(i).setReleaseDate(sortedList.get(i + 1).getReleaseDate());
                    sortedList.get(i).setDuration(sortedList.get(i + 1).getDuration());
                    sortedList.get(i).setLikes(sortedList.get(i + 1).getLikes());

                    sortedList.get(i + 1).setId(temp.getId());
                    sortedList.get(i + 1).setName(temp.getName());
                    sortedList.get(i + 1).setDescription(temp.getDescription());
                    sortedList.get(i + 1).setReleaseDate(temp.getReleaseDate());
                    sortedList.get(i + 1).setDuration(temp.getDuration());
                    sortedList.get(i + 1).setLikes(temp.getLikes());
                }
            }
        }
        // Удаляем элементы из отсортированного списка, индексы которых больше sizeOfList или больше 10-ти,
        // если sizeOfList не задан:
        for (int i = sortedList.size(); i > sizeOfList; i--) {
            sortedList.remove(i - 1);
        }

        return sortedList;
    }
}
