package ru.yandex.practicum.model;

public class FriendshipStatus {
    private Long statusId;
    private String friendshipStatusName;

    public FriendshipStatus(Long userId, String friendshipStatus) {
        this.statusId = userId;
        this.friendshipStatusName = friendshipStatus;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long userId) {
        this.statusId = userId;
    }

    public String getFriendshipStatusName() {
        return friendshipStatusName;
    }

    public void setFriendshipStatusName(String friendshipStatusName) {
        this.friendshipStatusName = friendshipStatusName;
    }
}
