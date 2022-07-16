// В пакете storage хранятся только классы и интерфейсы, имеющие отношение к хранению данных.
package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Set<User> users = new HashSet<>();

    private Long countOfUsers = 0L;

    @Override
    public User createUser(User user) {
        try {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Введена дата рождения пользователя в будущем: {}", user.getBirthday());
                throw new ValidationException("Дата рождения пользователя не может быть в будущем. " +
                        "Запись о пользователе не была добавлена.");
            }

            if (user.getName().isEmpty()) {
                log.warn("Поскольку имя пользователя не введено, вместо него взято значение login: {}", user.getLogin());
                user.setName(user.getLogin());
            }

            user.setId(++countOfUsers);
            users.add(user);
            log.info("Добавлен пользователь {}", user);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    @Override
    public User updateUser(User user) {
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


                    if (user.getName().isEmpty()) {
                        userInUsers.setName(user.getLogin());
                    } else {
                        userInUsers.setName(user.getName());
                    }

                    userInUsers.setEmail(user.getEmail());
                    userInUsers.setBirthday(user.getBirthday());
                    userInUsers.setFriends(user.getFriends());

                    log.info("Обновлён пользователь {}", user);
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

        return user;
    }

    @Override
    public Set<User> returnUsers() {
        return users;
    }

    @Override
    public User getUserById(Long userId) {
        try {
            for (User user : users) {
                if (user.getId() == userId) {
                    return user;
                }
            }

            log.error("Попытка получения несуществующего пользователя (нет совпадений по id ).");
            throw new ValidationException("Пользователя с таким id не существует.");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
