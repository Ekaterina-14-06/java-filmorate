package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import java.util.Set;

@Validated
@RestController
@Slf4j
public class UserController {

    private final FilmService filmService;                  // для организации доступа к FilmService из FilmController
    private final UserService userService;                  // для организации доступа к UserService из FilmController
    private final InMemoryFilmStorage inMemoryFilmStorage;  // для организации доступа к inMemoryFilmStorage из FilmController
    private final InMemoryUserStorage inMemoryUserStorage;  // для организации доступа к inMemoryUserStorage из FilmController

    @Autowired  // Внедрение зависимостей.
    public UserController(FilmService filmService,
                          UserService userService,
                          InMemoryFilmStorage inMemoryFilmStorage,
                          InMemoryUserStorage inMemoryUserStorage) {
        this.filmService = filmService;
        this.userService = userService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // ЭНДПОИНТ: получение списка всех пользователей (согласно ТЗ спринта 8)
    @GetMapping("/users")
    public Set<User> getAllUsers() {
        return inMemoryUserStorage.returnUsers();
    }

    // ЭНДПОИНТ: создание пользователя (согласно ТЗ спринта 8)
    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.createUser(user);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    // ЭНДПОИНТ: обновление пользователя (согласно ТЗ спринта 8)
    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    // ЭНДПОИНТ: получение пользователя по уникальному идентификатору (согласно ТЗ спринта 9)
    // GET /users/{id}
    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return inMemoryUserStorage.getUserById(id);
    }

    // ЭНДПОИНТ: возвращение списка пользователей, являющихся друзьями пользователя с заданным идентификатором
    // (согласно ТЗ спринта 9)
    // GET /users/{id}/friends
    @GetMapping(value = "/users/{id}/friends")
    public Set<User> getFriendsOfUserById(@PathVariable Long id) {
        return userService.getFriendsOfUserById(id);
    }

    // ЭНДПОИНТ: возвращение списка друзей, общих с другим пользователем (согласно ТЗ спринта 9)
    // GET /users/{id}/friends/common/{otherId}
    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    // ЭНДПОИНТ: добавление пользователя в друзья (согласно ТЗ спринта 9)
    // PUT /users/{id}/friends/{friendId}
    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriendById(id, friendId);
    }

    // ЭНДПОИНТ: удаление пользователя из друзей (согласно ТЗ спринта 9)
    // DELETE /users/{id}/friends/{friendId}
    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendById(id, friendId);
    }
}
