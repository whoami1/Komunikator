package pl.wrzesien;

import java.io.Serializable;

/**
 * Created by Michał Wrzesień on 2015-03-28.
 */

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1;


    private String userNick;
    private boolean userStatus;

    public UserInfo(String userNick, boolean userStatus) {
        this.userNick = userNick;
        this.userStatus = userStatus;
    }

    public String getUserNick() {

        return userNick;
    }

    public boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userNick='" + userNick + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }
}
