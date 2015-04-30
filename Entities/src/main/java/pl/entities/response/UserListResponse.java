package pl.entities.response;

import pl.wrzesien.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-14.
 */
public class UserListResponse implements Serializable {
    private static final long serialVersionUID = 1;

    private List<UserInfo> userInfoList;

    public UserListResponse(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    @Override
    public String toString() {
        return "pl.entities.response.UserListResponse{" +
                "userInfoList=" + userInfoList +
                '}';
    }
}
