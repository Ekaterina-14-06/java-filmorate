package ru.yandex.practicum.model.model;

import java.time.LocalDate;
import java.time.Duration;
import lombok.Data;
import lombok.NonNull;

@Data
public class Film {
    @NonNull
    private int id;           // id - целочисленный идентификатор фильма
    @NonNull
    private String name;            // name - название фильма (не может быть пустым)
    private String description;     // description - описание фильма (максимальная длина - 200 символов)
    private LocalDate releaseDate;  // releaseDate - дата релиза (не ранее 28.12.1895 г.)
    private Duration duration;      // duration - продолжительность фильма (должна быть положительной)

}
