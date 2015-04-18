package pl.wrzesien;

import java.io.Serializable;

/**
 * Created by Michał Wrzesień on 2015-03-28.
 */

public class User implements Serializable {
    private static final long serialVersionUID = 1;
    private Integer userId;
    private String userNick;
    private String userPassword;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(String userNick) {
        this.userNick = userNick;
    }

    public User(String userNick, String userPassword) {
        this.userNick = userNick;
        this.userPassword = userPassword;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userNick='" + userNick + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
        if (userNick != null ? !userNick.equals(user.userNick) : user.userNick != null) return false;
        if (userPassword != null ? !userPassword.equals(user.userPassword) : user.userPassword != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userNick != null ? userNick.hashCode() : 0);
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        return result;
    }
}
