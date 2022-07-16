package ru.yandex.practicum.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.HashSet;
import java.util.Set;

public class UserDbService {
    private final JdbcTemplate jdbcTemplate;

    public UserDbService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriendById(Long userId1, Long userId2, Long statusId) {
        jdbcTemplate.queryForRowSet("INSERT INTO friends (id_user, id_friend, id_status) VALUES (?, ?, ?)",
                userId1, userId2, statusId);
    }

    public void deleteFriendById(Long userId1, Long userId2) {
        jdbcTemplate.queryForRowSet("DELETE * FROM friends WHERE id_user = ? AND id_friend = ?",
                userId1, userId2);
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
}