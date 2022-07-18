package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.model.User;

import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    Set<User> returnUsers();

    User getUserById(Long userId);
}
