// В пакете storage только классы и интерфейсы, имеющие отношение к хранению данных.
package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import java.util.Set;

// Согласно ТЗ спринта 9 интерфейс UserStorage определяет методы добавления, удаления и модификации объектов (пользователей).
public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    Set<User> returnUsers();
    User getUserById(Long userId) throws ValidationException;
    void deleteUser(User user);
    void deleteUserById(Long userId);
}
