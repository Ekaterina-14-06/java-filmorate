package ru.yandex.practicum.model.controller;

// import org.slf4j.Logger;         // Не пригодилось, поскольку используем import lombok.extern.slf4j.Slf4j для аннотации @Slf4j
// import org.slf4j.LoggerFactory;  // Не пригодилось, поскольку используем import lombok.extern.slf4j.Slf4j для аннотации @Slf4j
import com.sun.net.httpserver.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import ru.yandex.practicum.model.model.User;
import ru.yandex.practicum.model.exceptions.ValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.sun.net.httpserver.HttpExchange;

@RestController
@Slf4j
public class UserController {
    private final Set<User> users = new HashSet<>();
    // создаём ЛОГЕР - Не понадобилось, поскольку используем аннотацию @Slf4j
    //private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private int count = -1;

    // ЭНДПОИНТ: получение списка всех пользователей
    @GetMapping("/users")
    public Set<User> getAllUsers() {
        // Согласно ТЗ, необходимо добавить логирование только для операций, которые изменяют сущности — добавляют и обновляют их.
        // log.info("Количество пользователей {}", users.size());
        return users;
    }

    // ЭНДПОИНТ: создание пользователя
    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) throws IOException {
        try {
            /*
            // Убрала эту часть кода, поскольку увидела, что в запросе POST в тестовых примерах в теле (body)
            // не передаётся id нового (создаваемого) пользователя
            for (User userInUsers : users) {
                if (userInUsers.getId() == user.getId()) {
                    log.error("Ошибка при создании пользователя с уже имеющимся id: {}", user.getId());
                    throw new ValidationException("Пользователь с таким id уже существует. " +
                                                  "Запись о пользователе не была добавлена.");
                }
            }
            */

            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Введена дата рождения пользователя в будущем: {}", user.getBirthday());
                throw new ValidationException("Дата рождения пользователя не может быть в будущем. " +
                                              "Запись о пользователе не была добавлена.");
            }

            // Если поле name не заполнено, тогда в качестве его значение будет использовано значение поля login
            if (user.getName().isEmpty()) {
            //if (user.getName().equals("")) {
                log.warn("Поскольку имя пользователя не введено, вместо него взято значение login: {}", user.getLogin());
                user.setName(user.getLogin());
            }

            user.setId(++count);
            users.add(user);
            log.info("Добавлен пользователь {}", user.toString());
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return user;  // возвращение сущности - согласно ТЗ
    }

    // ЭНДПОИНТ: обновление пользователя
    @PutMapping(value = "/users")  // или PatchMapping?
    public User updateUser(@Valid @RequestBody User user) {
        try {
            boolean isPresent = false;
            for (User userInUsers : users) {
                if (userInUsers.getId() == user.getId()) {
                    isPresent = true;

                    if (user.getBirthday().isAfter(LocalDate.now())) {
                        log.error("Введена дата рождения пользователя в будущем: {}", user.getBirthday());
                        throw new ValidationException("Дата рождения пользователя не может быть в будущем. " +
                                                      "Запись о пользователе не была обновлена.");
                    }

                    userInUsers.setLogin(user.getLogin());

                    // Если поле name не заполнено, тогда в качестве его значение будет использовано значение поля login
                    if (user.getName().isEmpty()) {
                    //if (user.getName().equals("")) {
                        userInUsers.setName(user.getLogin());
                    } else {
                        userInUsers.setName(user.getName());
                    }

                    userInUsers.setEmail(user.getEmail());
                    userInUsers.setBirthday(user.getBirthday());

                    log.info("Обновлён пользователь {}", user.toString());
                    break;
                }
            }

            if (!isPresent) {
                log.error("Попытка изменить свойства несуществующего пользователя (нет совпадений по id {}).", user.getId());
                throw new ValidationException("Пользователя с таким id не существует (некого обновлять). " +
                                              "Запись о пользователе не была обновлена.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return user;  // возвращение сущности - согласно ТЗ
    }
}
