package ru.yandex.practicum.service;  // Пакет service объединяет бизнес-логику.

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.storage.user.InMemoryUserStorage;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;  // для организации доступа к inMemoryUserStorage из UserService

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriend(User user1, User user2) {
        try {
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

    public void deleteFriend(User user1, User user2) {
        try {
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

    public Set<Long> returnIdOfCommonFriends(User user1, User user2) {
        Set<Long> idOfCommonFriends = new HashSet<>();
        for (Long idFriendOfUser1 : user1.getFriends()) {
            for (Long idFriendOfUser2 : user2.getFriends()) {
                if (idFriendOfUser1 == idFriendOfUser2) {
                    idOfCommonFriends.add(idFriendOfUser1);
                }
            }
        }
        return idOfCommonFriends;
    }

    public Set<User> returnCommonFriends(Set<User> users, User user1, User user2) {
        Set<User> commonFriends = new HashSet<>();
        for (Long idFriendOfUser1 : user1.getFriends()) {
            for (Long idFriendOfUser2 : user2.getFriends()) {
                if (idFriendOfUser1 == idFriendOfUser2) {
                    for (User user : users) {
                        if (user.getId() == idFriendOfUser1) {
                            commonFriends.add(user);
                        }
                        break;
                    }
                    break;
                }
            }
        }
        return commonFriends;
    }
}
