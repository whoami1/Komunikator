package pl.entities.response;

import pl.wrzesien.User;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-26.
 */
public class AllUsersListResponse implements Serializable {
    private static final long serialVersionUID = 1;

    private List<User> allUsersList;

    public AllUsersListResponse(List<User> allUsersList) {
        this.allUsersList = allUsersList;
    }

    public List<User> getAllUsersList() {
        return allUsersList;
    }

    @Override
    public String toString() {
        return "AllUsersListResponse{" +
                "allUsersList=" + allUsersList +
                '}';
    }
}