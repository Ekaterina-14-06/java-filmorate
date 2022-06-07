package ru.yandex.practicum.model.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class User {
    @NonNull
    private final int id;        // id - целочисленный идентификатор
    @NonNull
    @Email
    private String email;        // email - электронная почта (не может быть пустой, должна содержать символ @)
    @NonNull
    @NotBlank
    private String login;        // login - логин пользователя (не может быть пустым и содержать пробелы)
    private String name;         // name - имя для отображения (может быть пустым, тогда будет использован логин)
    private LocalDate birthday;  // birthday - дата рождения (не может быть в будущем)
}
