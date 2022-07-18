package ru.yandex.practicum.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.dao.UserDao;
import ru.yandex.practicum.model.User;
import ru.yandex.practicum.model.User.Friendship;

import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserDbStorage implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User createUser(User user) {
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("INSERT INTO users (name,  email, login, birthday) " +
                        "VALUES (?, ?, ?, ?)",
                user.getName(), user.getEmail(),
                user.getLogin(), user.getBirthday());
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT id FROM users WHERE login = ?", user.getLogin());
        userRows.next();
        user.setId(userRows.getLong("id"));

        for (Friendship f : user.getFriendsDb()) {
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("INSERT INTO friends (id_user, id_friend, id_status) " +
                            "VALUES (?, ?, ?)",
                    userRows.getLong("id"),
                    f.getFriendId(),
                    f.getStatusId());
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("UPDATE users SET name = ?, email = ?, login = ?, birthday = ? " +
                        "WHERE id = ?", user.getName(), user.getEmail(), user.getLogin(),
                user.getBirthday(), user.getId());

        for (User.Friendship f : user.getFriendsDb()) {
            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("UPDATE friends SET id_friend = ?, id_status WHERE id_user = ?",
                    f.getFriendId(), f.getStatusId(), user.getId());
        }

        return user;
    }

    @Override
    public Set<User> returnUsers() {
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        Set<User> users = new HashSet<>();
        while (usersRows.next()) {
            User user = new User();
            user.setId(usersRows.getLong("id"));
            user.setName(usersRows.getString("name"));
            user.setLogin(usersRows.getString("login"));
            user.setEmail(usersRows.getString("email"));
            user.setBirthday(usersRows.getDate("birthday").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT * FROM friends WHERE id = ?", user.getId());
            while (friendsRows.next()) {
                Friendship friendship = new Friendship(friendsRows.getLong("id_friend"),
                        friendsRows.getLong("id_status"));
                user.getFriendsDb().add(friendship);
            }

            users.add(user);
        }
        return users;
    }

    @Override
    public User getUserById(Long userId) {
        SqlRowSet usersRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id = ?", userId);
        if (usersRows.next()) {
            User user = new User();
            user.setId(userId);
            user.setName(usersRows.getString("name"));
            user.setLogin(usersRows.getString("login"));
            user.setEmail(usersRows.getString("email"));
            user.setBirthday(usersRows.getDate("birthday").toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            SqlRowSet friendsRows = jdbcTemplate.queryForRowSet("SELECT * FROM friends WHERE id = ?", userId);
            while (friendsRows.next()) {
                Friendship friendship = new Friendship(friendsRows.getLong("id_friend"),
                        friendsRows.getLong("id_status"));
                user.getFriendsDb().add(friendship);
            }
            return user;
        } else {
            return null;
        }
    }

    @Override
    public void deleteUsers() {
        jdbcTemplate.queryForRowSet("DELETE * FROM users");
        jdbcTemplate.queryForRowSet("DELETE * FROM friends");
        jdbcTemplate.queryForRowSet("DELETE * FROM likes");
    }

    @Override
    public void deleteUserById(Long userId) {
        jdbcTemplate.queryForRowSet("DELETE * FROM friends WHERE id = ?", userId);
        jdbcTemplate.queryForRowSet("DELETE * FROM likes WHERE id = ?", userId);
        jdbcTemplate.queryForRowSet("DELETE * FROM users WHERE id = ?", userId);
    }
}
