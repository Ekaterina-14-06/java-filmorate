package ru.yandex.practicum.service;

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
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public void addFriendById(Long id1, Long id2) {
        try {
            User user1 = inMemoryUserStorage.getUserById(id1);
            User user2 = inMemoryUserStorage.getUserById(id2);

            boolean isExistFriendWithThisId = false;
            for (Long id : user1.getFriendsIds()) {
                if (id == user2.getId()) {
                    isExistFriendWithThisId = true;
                    break;
                }
            }

            if (!isExistFriendWithThisId) {
                user1.getFriendsIds().add(user2.getId());
                user2.getFriendsIds().add(user1.getId());
            } else {
                log.error("Попытка повторного добавления пользователя в друзья.");
                throw new ValidationException("Этот пользователь уже добавлен в друзья. " +
                        "Повторное добавление не произведено.");
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
            for (Long id : user1.getFriendsIds()) {
                if (id == user2.getId()) {
                    user1.getFriendsIds().remove(user2.getId());
                    isExistFriendWithThisId = true;
                    break;
                }
            }

            if (!isExistFriendWithThisId) {
                log.error("Попытка удаления из друзей пользователя, не находящегося в друзьях.");
                throw new ValidationException("Запрашиваемого пользователя нет в друзьях. " +
                        "Удаление его из друзей не произведено.");
            }
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }
}
