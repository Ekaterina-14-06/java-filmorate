package ru.yandex.practicum.model;

import java.time.LocalDate;
import java.time.Duration;
import java.util.Set;
import lombok.Data;
// import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
// import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Validated
@Data
public class Film {
    //@NonNull
    private Long id;                // поле id - целочисленный идентификатор фильма
    //@NonNull
    //@NotBlank
    @NotEmpty
    private String name;            // поле name - название фильма (не может быть пустым)
    private String description;     // поле description - описание фильма (максимальная длина - 200 символов)
    private LocalDate releaseDate;  // поле releaseDate - дата релиза (не ранее 28.12.1895 г.)
    private Duration duration;      // поле duration - продолжительность фильма (должна быть положительной)
    private Set<Long> likes;        // поле likes - хранит информацию о лайках фильма пользователями

}
