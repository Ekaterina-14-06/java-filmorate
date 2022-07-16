package ru.yandex.practicum.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.model.FriendshipStatus;
import ru.yandex.practicum.model.User;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

public class UserDbService {
    private final JdbcTemplate jdbcTemplate;

    public UserDbService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================================= FRIENDS ===========================================================

    public void addFriend(User user1, User user2, FriendshipStatus status) {
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("INSERT INTO friends (id_user, id_friend, id_status) " +
                        "VALUES (?, ?, ?)",
                user1.getId(), user2.getId(), status.getStatusId());
    }

    public void addFriendById(Long userId1, Long userId2, Long statusId) {
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("INSERT INTO friends (id_user, id_friend, id_status) " +
                        "VALUES (?, ?, ?)",
                userId1, userId2, statusId);
    }

    public void deleteFriend(User user1, User user2) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("DELETE * FROM friends WHERE id_user = ? AND id_friend = ?",
                user1.getId(), user2.getId());
    }

    public void deleteFriendById(Long userId1, Long userId2) {
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("DELETE * FROM friends WHERE id_user = ? AND id_friend = ?",
                userId1, userId2);
    }

    public void deleteDeniedFriendById(Long userId1) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT id FROM statuses WHERE name = ?", "denied");
        if (statusRows.next()) {
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet("DELETE * FROM friends WHERE id_user = ? AND id_friend = ?",
                    userId1, statusRows.getLong("id"));
        }
    }

    public void deleteFriends() {
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("DELETE * FROM friends");
    }

    public Set<Long> returnIdOfFriends(Long userId) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT id FROM statuses WHERE name = ?",
                "confirmed");
        if (statusRows.next()) {
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT id_friend WHERE id_user = ? AND id_status = ?",
                    userId, statusRows.getLong("id"));
            Set<Long> friendsIds = new HashSet<>();
            while (friendsRows.next()) {
                friendsIds.add(friendsRows.getLong("id_friend"));
            }
            return friendsIds;
        } else {
            return null;
        }
    }

    public Set<User> returnCommonFriends(User user1, User user2) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT id FROM statuses WHERE name = ?",
                "confirmed");
        if (statusRows.next()) {
            Long statusId = statusRows.getLong("id");
            SqlRowSet commonFriendsRows = jdbcTemplate.queryForRowSet(
                    "SELECT t1.id_friend AS id" +
                            "FROM (SELECT id_friend FROM friends WHERE id_user = ? AND id_status = ?) AS t1 " +
                            "INNER JOIN (SELECT id_friend FROM friends WHERE id_user = ? AND id_status = ?) AS t2 " +
                            "ON t1.id_friend = t2.id_friend",
                    user1.getId(), statusId, user2.getId(), statusId);

            Set<User> users = new HashSet<>();
            while (commonFriendsRows.next()) {
                SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?",
                        commonFriendsRows.getLong("id"));
                User tempUser = new User();
                tempUser.setId(userRows.getLong("id"));
                tempUser.setName(userRows.getString("name"));
                tempUser.setEmail(userRows.getString("email"));
                tempUser.setLogin(userRows.getString("login"));
                tempUser.setBirthday(userRows.getDate("birthday").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(
                        "SELECT id_friend, id_status FROM friends WHERE id_user = ?",
                        tempUser.getId());
                while (friendsRows.next()) {
                    User.Friendship friend = new User.Friendship(friendsRows.getLong("id_friend"),
                            friendsRows.getLong("id_status"));
                    tempUser.getFriendsDb().add(friend);
                }

                users.add(tempUser);
            }
            return users;
        } else {
            return null;
        }
    }

    public Set<Long> returnCommonFriendsById(Long userId1, Long userId2) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT id FROM statuses WHERE name = ?",
                "confirmed");
        if (statusRows.next()) {
            Long statusId = statusRows.getLong("id");
            SqlRowSet commonFriendsRows = jdbcTemplate.queryForRowSet(
                    "SELECT t1.id_friend AS id" +
                            "FROM (SELECT id_friend FROM friends WHERE id_user = ? AND id_status = ?) AS t1 " +
                            "INNER JOIN (SELECT id_friend FROM friends WHERE id_user = ? AND id_status = ?) AS t2 " +
                            "ON t1.id_friend = t2.id_friend",
                    userId1, statusId, userId2, statusId);

            Set<Long> userIds = new HashSet<>();
            while (commonFriendsRows.next()) {
                userIds.add(commonFriendsRows.getLong("id"));
            }
            return userIds;
        } else {
            return null;
        }
    }

    // ============================================= STATUSES ==========================================================

    public FriendshipStatus addStatus (FriendshipStatus friendshipStatus) {
        SqlRowSet statusesRows = jdbcTemplate.queryForRowSet("INSERT INTO statuses (name) VALUES (?)",
                friendshipStatus.getFriendshipStatusName());
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT id FROM statuses WHERE name = ?",
                friendshipStatus.getFriendshipStatusName());
        if (statusRows.next()) {
            friendshipStatus.setStatusId(statusRows.getLong("id"));
        }
        return friendshipStatus;
    }

    public Set<FriendshipStatus> getStatuses () {
        SqlRowSet statusesRows = jdbcTemplate.queryForRowSet("SELECT * FROM statuses");
        Set<FriendshipStatus> statuses = new HashSet<>();
        while (statusesRows.next()) {
            FriendshipStatus friendshipStatus = new FriendshipStatus(statusesRows.getLong("id"),
                    statusesRows.getString("name"));
            statuses.add(friendshipStatus);
        }
        return statuses;
    }

    public FriendshipStatus getStatusById (Long statusId) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("SELECT * FROM statuses WHERE id = ?", statusId);
        if (statusRows.next()) {
            FriendshipStatus fs = new FriendshipStatus(statusRows.getLong("id"),
                    statusRows.getString("name"));
            return fs;
        } else {
            return null;
        }
    }

    public FriendshipStatus updateStatusById (FriendshipStatus friendshipStatus) {
        SqlRowSet statusRows = jdbcTemplate.queryForRowSet("UPDATE statuses SET name = ? WHERE id = ?",
                friendshipStatus.getFriendshipStatusName(), friendshipStatus.getStatusId());
        return friendshipStatus;
    }

    public void deleteStatuses () {
        SqlRowSet statusesRows = jdbcTemplate.queryForRowSet("DELETE * FROM statuses");
    }

    public void deleteStatusById (Long statusId) {
        SqlRowSet statusesRows = jdbcTemplate.queryForRowSet("DELETE * FROM statuses WHERE id = ?", statusId);
    }
}