package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.User;

import java.util.Set;

public interface UserDao {
    User createUser(User user);

    User updateUser(User user);

    Set<User> returnUsers();

    User getUserById(Long userId);

    void deleteUsers();

    void deleteUserById(Long userId);
}
