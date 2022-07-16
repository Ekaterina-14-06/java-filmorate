package ru.yandex.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.service.UserDbService;
import ru.yandex.practicum.service.UserService;
import ru.yandex.practicum.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;
import java.util.Set;

@Validated
@RestController
@Slf4j
public class UserController {

    private final FilmService filmService;
    private final UserService userService;
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserDbService userDbService;

    @Autowired  // Внедрение зависимостей.
    public UserController(FilmService filmService,
                          UserService userService,
                          InMemoryFilmStorage inMemoryFilmStorage,
                          InMemoryUserStorage inMemoryUserStorage, UserDbService userDbService) {
        this.filmService = filmService;
        this.userService = userService;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userDbService = userDbService;
    }

    @GetMapping("/users")
    public Set<User> getAllUsers() {
        return inMemoryUserStorage.returnUsers();
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.createUser(user);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        return inMemoryUserStorage.updateUser(user);  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return inMemoryUserStorage.getUserById(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriendById(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriendById(id, friendId);
    }

    @GetMapping(value = "/users/{idFirst}/common/{idSecond}")
    public Set<Long> getCommonFriends(@PathVariable Long idFirst, @PathVariable Long idSecond) {
        return userDbService.returnCommonFriendsById(idFirst, idSecond);
    }
}
