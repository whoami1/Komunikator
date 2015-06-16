package pl.wrzesien;

import java.io.Serializable;

public class UserInfo implements Serializable
{
    private String userNick;
    private boolean userStatus;

    public UserInfo(String userNick, boolean userStatus)
    {
        this.userNick = userNick;
        this.userStatus = userStatus;
    }

    public String getUserNick()
    {

        return userNick;
    }

    public String getUserStatus()
    {
        if (userStatus == true)
        {
            return "dostepny";
        }
        return "niedostepny";
    }

    public void setUserStatus(boolean userStatus)
    {
        this.userStatus = userStatus;
    }

    @Override
    public String toString()
    {
        return "UserInfo{" +
                "userNick='" + userNick + '\'' +
                ", userStatus=" + userStatus +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (userStatus != userInfo.userStatus) return false;
        return !(userNick != null ? !userNick.equals(userInfo.userNick) : userInfo.userNick != null);

    }

    @Override
    public int hashCode()
    {
        int result = userNick != null ? userNick.hashCode() : 0;
        result = 31 * result + (userStatus ? 1 : 0);
        return result;
    }
}
