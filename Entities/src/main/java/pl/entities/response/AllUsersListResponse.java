package pl.entities.response;

import pl.wrzesien.UserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-26.
 */
public class AllUsersListResponse implements Serializable {
    private static final long serialVersionUID = 1;

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