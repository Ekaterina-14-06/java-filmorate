// В пакете storage хранятся только классы и интерфейсы, имеющие отношение к хранению данных.
package ru.yandex.practicum.storage.user;

import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component  // Аннотация @Component - для внедрения зависимостей и передачи хранилища сервисам.
// Согласно ТЗ спринта 9 класс InMemoryUserStorage содержит логику хранения, обновления и поиска объектов (пользователей).
public class InMemoryUserStorage implements UserStorage {

    // Поле users - содержит список пользователей
    private final Set<User> users = new HashSet<>();

    // Поле countOfUsers - счётчик (определяет значение поля id)
    private Long countOfUsers = 0L;

    @Override
    // Создание пользователя
    public User createUser(User user) {
        try {
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Введена дата рождения пользователя в будущем: {}", user.getBirthday());
                throw new ValidationException("Дата рождения пользователя не может быть в будущем. " +
                                              "Запись о пользователе не была добавлена.");
            }

            // Если поле name не заполнено, тогда в качестве его значение будет использовано значение поля login.
            if (user.getName().isEmpty()) {
            // if (user.getName().equals("")) {
                log.warn("Поскольку имя пользователя не введено, вместо него взято значение login: {}", user.getLogin());
                user.setName(user.getLogin());
            }

            user.setId(++countOfUsers);
            users.add(user);
            log.info("Добавлен пользователь {}", user);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

        return user;  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @Override
    // Обновление пользователя
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

                    // Если поле name не заполнено, тогда в качестве его значение будет использовано значение поля login
                    if (user.getName().isEmpty()) {
                    // if (user.getName().equals("")) {
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

        return user;  // Возвращение сущности - согласно ТЗ спринта 8.
    }

    @Override
    // Получение списка всех пользователей
    public Set<User> returnUsers() {
        return users;
    }

    @Override
    // Получение пользователя по его id
    public User getUserById(Long userId) throws ValidationException {
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

    @Override
    public void deleteUser(User user) {
        try {
            if (!users.contains(user)) {
                log.error("Попытка удаления несуществующего пользователя: {}", user);
                throw new ValidationException("Попытка удаления несуществующего фильма.");
            }

            users.remove(user);
            log.info("Удалён фильм {}", user);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = null;
        try {
            user = getUserById(userId);
        } catch (ValidationException e) {
            e.printStackTrace();
        }

        try {
            if (!users.contains(user)) {
                log.error("Попытка удаления несуществующего фильма: {}", user);
                throw new ValidationException("Попытка удаления несуществующего фильма.");
            }

            users.remove(user);
            log.info("Удалён фильм {}", user);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
