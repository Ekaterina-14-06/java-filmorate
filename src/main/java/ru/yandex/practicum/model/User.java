package ru.yandex.practicum.model;

import java.time.LocalDate;
import java.util.Set;
import lombok.Data;
// import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.Email;
// import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Validated
@Data
public class User {
    //@NonNull
    private Long id;             // поле id - целочисленный идентификатор
    //@NonNull
    //@NotBlank
    @NotEmpty
    @Email
    private String email;        // поле email - электронная почта (не может быть пустой, должна содержать символ @)
    //@NonNull
    //@NotBlank
    @NotEmpty
    private String login;        // поле login - логин пользователя (не может быть пустым и содержать пробелы)
    private String name;         // поле name - имя для отображения (может быть пустым, тогда будет использован логин)
    private LocalDate birthday;  // поле birthday - дата рождения (не может быть в будущем)
    private Set<Long> friends;   // поле friends - хранит информацию о друзьях пользователя
}
