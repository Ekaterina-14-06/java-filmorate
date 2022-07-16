package ru.yandex.practicum.model;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Validated
@Data
public class User {
    private Long id;
    @NotEmpty
    private String login;
    private String name;
    @NotEmpty
    @Email
    private String email;
    private LocalDate birthday;
    private Set<Long> friendsIds;
    private Set<Friendship> friendsDb;

    public static class Friendship {
        private Long friendId;
        private Long statusId;

        public Friendship(Long userId, Long statusId) {
            this.friendId = userId;
            this.statusId = statusId;
        }

        public Long getFriendId() {
            return friendId;
        }

        public void setFriendId(Long friendId) {
            this.friendId = friendId;
        }

        public Long getStatusId() {
            return statusId;
        }

        public void setStatusId(Long statusId) {
            this.statusId = statusId;
        }
    }
}