package pl.entities.response;

import pl.wrzesien.UserInfo;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class MessageResponse implements Serializable {
    private static final long serialVersionUID = 1;

    private UserInfo userInfo;
    private String message;

    public MessageResponse(UserInfo userInfo, String message) {

        this.userInfo = userInfo;
        this.message = message;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "userInfo=" + userInfo +
                ", message='" + message + '\'' +
                '}';
    }
}
