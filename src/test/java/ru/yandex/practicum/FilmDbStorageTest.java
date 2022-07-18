package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.FilmDbService;
import ru.yandex.practicum.storage.film.FilmDbStorage;
import ru.yandex.practicum.storage.user.UserDbStorage;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)

public class FilmDbStorageTest {

    FilmDbStorage filmDbStorage = new FilmDbStorage(new JdbcTemplate());
    UserDbStorage userDbStorage = new UserDbStorage(new JdbcTemplate());

    @Test
    void createFilmTestCorrect () {
        Film film = new Film();
        film.setId(1L);
        film.setName("film1name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(2021, Month.APRIL, 15));
        film.setDuration(120);
        film.setRatingId(1L);
        film.getLikesDb().add(new Film.Likes(1L, 1L));
        film.getGenresDb().add(new Film.Genres(1L, 1L));
        Assertions.assertEquals(film.getName(), filmDbStorage.createFilm(film).getName());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getReleaseDate(), filmDbStorage.createFilm(film).getReleaseDate());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getRatingId(), filmDbStorage.createFilm(film).getRatingId());
        Assertions.assertEquals(film.getLikesDb(), filmDbStorage.createFilm(film).getLikesDb());
        Assertions.assertEquals(film.getGenresDb(), filmDbStorage.createFilm(film).getGenresDb());
    }

    @Test
    void createFilmTestWrongDate () {
        Film film = new Film();
        film.setId(1L);
        film.setName("film2name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1900, Month.APRIL, 15));
        film.setDuration(120);
        film.setRatingId(1L);
        film.getLikesDb().add(new Film.Likes(1L, 1L));
        film.getGenresDb().add(new Film.Genres(1L, 1L));
        Assertions.assertEquals(film.getName(), filmDbStorage.createFilm(film).getName());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getReleaseDate(), filmDbStorage.createFilm(film).getReleaseDate());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getRatingId(), filmDbStorage.createFilm(film).getRatingId());
        Assertions.assertEquals(film.getLikesDb(), filmDbStorage.createFilm(film).getLikesDb());
        Assertions.assertEquals(film.getGenresDb(), filmDbStorage.createFilm(film).getGenresDb());
    }

    @Test
    void createFilmTestWrongDescription () {
        Film film = new Film();
        film.setId(1L);
        film.setName("film3name");
        film.setDescription("film1na000film1na000film1na000film1na000film1na000film1na000film1na000film1na000film1na000" +
                "film1na000film1na000film1na000film1na000film1na000film1na000film1na000film1na000film1na000film1" +
                "na000film1na0006546464165");
        film.setReleaseDate(LocalDate.of(1900, Month.APRIL, 15));
        film.setDuration(120);
        film.setRatingId(1L);
        film.getLikesDb().add(new Film.Likes(1L, 1L));
        film.getGenresDb().add(new Film.Genres(1L, 1L));
        Assertions.assertEquals(film.getName(), filmDbStorage.createFilm(film).getName());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getReleaseDate(), filmDbStorage.createFilm(film).getReleaseDate());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getRatingId(), filmDbStorage.createFilm(film).getRatingId());
        Assertions.assertEquals(film.getLikesDb(), filmDbStorage.createFilm(film).getLikesDb());
        Assertions.assertEquals(film.getGenresDb(), filmDbStorage.createFilm(film).getGenresDb());
    }

    @Test
    void createFilmTestWrongDuration () {
        Film film = new Film();
        film.setId(1L);
        film.setName("film4name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.of(1900, Month.APRIL, 15));
        film.setDuration(-120);
        film.setRatingId(1L);
        film.getLikesDb().add(new Film.Likes(1L, 1L));
        film.getGenresDb().add(new Film.Genres(1L, 1L));
        Assertions.assertEquals(film.getName(), filmDbStorage.createFilm(film).getName());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getReleaseDate(), filmDbStorage.createFilm(film).getReleaseDate());
        Assertions.assertEquals(film.getDescription(), filmDbStorage.createFilm(film).getDescription());
        Assertions.assertEquals(film.getRatingId(), filmDbStorage.createFilm(film).getRatingId());
        Assertions.assertEquals(film.getLikesDb(), filmDbStorage.createFilm(film).getLikesDb());
        Assertions.assertEquals(film.getGenresDb(), filmDbStorage.createFilm(film).getGenresDb());
    }

    @Test
    void createUserCorrectTest() {
        User user = new User();
        user.setLogin("name15");
        user.setName("Name");
        user.setEmail("name@ya.ru");
        user.setBirthday(LocalDate.of(2000, Month.APRIL, 15));
        user.getFriendsIds().add(1L);
        user.getFriendsDb().add(new User.Friendship(1L, 1L));
        Assertions.assertEquals(user.getLogin(), userDbStorage.createUser(user).getLogin());
        Assertions.assertEquals(user.getName(), userDbStorage.createUser(user).getName());
        Assertions.assertEquals(user.getEmail(), userDbStorage.createUser(user).getEmail());
        Assertions.assertEquals(user.getBirthday(), userDbStorage.createUser(user).getBirthday());
        Assertions.assertEquals(user.getFriendsIds(), userDbStorage.createUser(user).getFriendsIds());
        Assertions.assertEquals(user.getFriendsDb(), userDbStorage.createUser(user).getFriendsDb());
    }

    @Test
    void createUserWrongBirthdayTest() {
        User user = new User();
        user.setLogin("name15");
        user.setName("Name");
        user.setEmail("name@ya.ru");
        user.setBirthday(LocalDate.of(2222, Month.APRIL, 15));
        user.getFriendsIds().add(1L);
        user.getFriendsDb().add(new User.Friendship(1L, 1L));
        Assertions.assertEquals(user.getLogin(), userDbStorage.createUser(user).getLogin());
        Assertions.assertEquals(user.getName(), userDbStorage.createUser(user).getName());
        Assertions.assertEquals(user.getEmail(), userDbStorage.createUser(user).getEmail());
        Assertions.assertEquals(user.getBirthday(), userDbStorage.createUser(user).getBirthday());
        Assertions.assertEquals(user.getFriendsIds(), userDbStorage.createUser(user).getFriendsIds());
        Assertions.assertEquals(user.getFriendsDb(), userDbStorage.createUser(user).getFriendsDb());
    }

    @Test
    void createUserWrongEmailTest() {
        User user = new User();
        user.setLogin("name15");
        user.setName("Name");
        user.setEmail("nameya.ru");
        user.setBirthday(LocalDate.of(2000, Month.APRIL, 15));
        user.getFriendsIds().add(1L);
        user.getFriendsDb().add(new User.Friendship(1L, 1L));
        Assertions.assertEquals(user.getLogin(), userDbStorage.createUser(user).getLogin());
        Assertions.assertEquals(user.getName(), userDbStorage.createUser(user).getName());
        Assertions.assertEquals(user.getEmail(), userDbStorage.createUser(user).getEmail());
        Assertions.assertEquals(user.getBirthday(), userDbStorage.createUser(user).getBirthday());
        Assertions.assertEquals(user.getFriendsIds(), userDbStorage.createUser(user).getFriendsIds());
        Assertions.assertEquals(user.getFriendsDb(), userDbStorage.createUser(user).getFriendsDb());
    }

    @Test
    void createUserWrongLoginTest() {
        User user = new User();
        user.setLogin("");
        user.setName("Name");
        user.setEmail("name@ya.ru");
        user.setBirthday(LocalDate.of(2000, Month.APRIL, 15));
        user.getFriendsIds().add(1L);
        user.getFriendsDb().add(new User.Friendship(1L, 1L));
        Assertions.assertEquals(user.getLogin(), userDbStorage.createUser(user).getLogin());
        Assertions.assertEquals(user.getName(), userDbStorage.createUser(user).getName());
        Assertions.assertEquals(user.getEmail(), userDbStorage.createUser(user).getEmail());
        Assertions.assertEquals(user.getBirthday(), userDbStorage.createUser(user).getBirthday());
        Assertions.assertEquals(user.getFriendsIds(), userDbStorage.createUser(user).getFriendsIds());
        Assertions.assertEquals(user.getFriendsDb(), userDbStorage.createUser(user).getFriendsDb());
    }
}
