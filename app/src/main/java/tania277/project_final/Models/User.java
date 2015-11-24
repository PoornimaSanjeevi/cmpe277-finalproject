package tania277.project_final.Models;

import java.util.List;

/**
 * Created by Tania on 11/16/15.
 */
public class User {
    private String UserId;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public List<User> getFriends() {
        return Friends;
    }

    public void setFriends(List<User> friends) {
        Friends = friends;
    }

    private String Name;
    private String Email;
    private String Avatar;
    private List<User> Friends;

    public List<RunRecord> getRunRecords() {
        return RunRecords;
    }

    public void setRunRecords(List<RunRecord> runRecords) {
        RunRecords = runRecords;
    }

    private List<RunRecord> RunRecords;

    public List<String> getFriendRequests() {
        return FriendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        FriendRequests = friendRequests;
    }

    private List<String> FriendRequests;

}
