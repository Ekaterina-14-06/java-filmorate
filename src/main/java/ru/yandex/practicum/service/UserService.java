package ru.yandex.practicum.service;  // Пакет service объединяет бизнес-логику.

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.UserStorage;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service  // Аннотация @Service - для получения доступа к этому классу UserService из контроллера UserController.
public class UserService {
// Согласно ТЗ спринта 9 класс UserService отвечает за следующие операции с пользователями:
// - добавление пользователя в друзья
//   (если пользователь1 стал другом пользователя2, то и пользователь2 стал другом пользователя1;
//    заявки в друзья одобрять не надо);
// - удаление пользователя из друзей;
// - вывод списка общих друзей.

    private final UserStorage inMemoryUserStorage;  // для организации доступа к inMemoryUserStorage из UserService

    @Autowired  // Внедрение зависимостей.
    public UserService(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    // Метод addFriendById добавляет пользователя в друзья.
    public void addFriendById(Long id1, Long id2) {
        try {
            User user1 = inMemoryUserStorage.getUserById(id1);
            User user2 = inMemoryUserStorage.getUserById(id2);

            boolean isExistFriendWithThisId = false;
            for (Long id : user1.getFriends()) {
                if (id == user2.getId()) {
                    isExistFriendWithThisId = true;
                    break;
                }
            }

            if (!isExistFriendWithThisId) {
                user1.getFriends().add(user2.getId());
                // если пользователь1 стал другом пользователя2, то и пользователь2 стал другом пользователя1
                user2.getFriends().add(user1.getId());
            } else {
                log.error("Попытка повторного добавления пользователя в друзья.");
                throw new ValidationException("Этот пользователь уже добавлен в друзья. " +
                        "Повторное добавление не произведено.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод deleteFriendById удаляет пользователя из друзей.
    public void deleteFriendById(Long id1, Long id2) {
        try {
            User user1 = inMemoryUserStorage.getUserById(id1);
            User user2 = inMemoryUserStorage.getUserById(id2);

            boolean isExistFriendWithThisId = false;
            for (Long id : user1.getFriends()) {
                if (id == user2.getId()) {
                    user1.getFriends().remove(user2.getId());
                    isExistFriendWithThisId = true;
                    break;
                }
            }

            if (!isExistFriendWithThisId) {
                // Не произошло удаление пользователя из друзей, поскольку его нет среди них.
                log.error("Попытка удаления из друзей пользователя, не находящегося в друзьях.");
                throw new ValidationException("Запрашиваемого пользователя нет в друзьях. " +
                                              "Удаление его из друзей не произведено.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод getFriendsOfUserById возвращает список пользователей,
    // являющихся друзьями пользователя с заданным идентификатором
    public Set<User> getFriendsOfUserById(Long userId) {
        User user = inMemoryUserStorage.getUserById(userId);
        Set<User> friendsOfUser = new HashSet<>();
        for (Long id : user.getFriends()) {
            friendsOfUser.add(inMemoryUserStorage.getUserById(id));
        }
        return friendsOfUser;
    }

    // Метод getCommonFriends возвращает список друзей, общих с другим пользователем
    // GET /users/{id}/friends/common/{otherId}
    public Set<User> getCommonFriends(Long userIdFirst, Long userIdSecond) {
        User userFirst = inMemoryUserStorage.getUserById(userIdFirst);
        User userSecond = inMemoryUserStorage.getUserById(userIdSecond);
        Set<User> friendsOfUser = new HashSet<>();
        for (Long id1 : userFirst.getFriends()) {
            for (Long id2 : userSecond.getFriends()) {
                if (id1 == id2) {
                    friendsOfUser.add(inMemoryUserStorage.getUserById(id1));
                }
            }
        }
        return friendsOfUser;
    }
}
