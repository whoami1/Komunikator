package pl.entities.response;

import pl.wrzesien.UserInfo;

import java.io.Serializable;
import java.util.List;


public class AllUsersListResponse implements Serializable {

    private List<UserInfo> allUsersList;

    public AllUsersListResponse(List<UserInfo> allUsersList) {
        this.allUsersList = allUsersList;
    }

    public List<UserInfo> getAllUsersList() {
        return allUsersList;
    }

    @Override
    public String toString() {
        return "AllUsersListResponse{" +
                "allUsersList=" + allUsersList +
                '}';
    }
}