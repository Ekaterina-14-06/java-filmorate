package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.util.Set;
import java.util.ArrayList;

@Slf4j
@Service
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;  // для организации доступа к inMemoryFilmStorage из FilmService
    private final InMemoryUserStorage inMemoryUserStorage;  // для организации доступа к inMemoryUserStorage из FilmService

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage,
                       InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

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

    public ArrayList<Film> getTopFilms(int sizeOfList) {
        ArrayList<Film> sortedList = new ArrayList<>();
        sortedList.addAll(inMemoryFilmStorage.getFilms());

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

        for (int i = sortedList.size(); i > sizeOfList; i--) {
            sortedList.remove(i - 1);
        }

        return sortedList;
    }
}
