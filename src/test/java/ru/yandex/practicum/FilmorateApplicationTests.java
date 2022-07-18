package ru.yandex.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmDbService;
import ru.yandex.practicum.storage.film.FilmDbStorage;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }
}
