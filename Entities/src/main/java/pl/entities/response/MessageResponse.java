package pl.entities.response;

import pl.wrzesien.User;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class MessageResponse implements Serializable {
    private static final long serialVersionUID = 1;

    private User user;
    private String message;

    public MessageResponse(User user, String message) {

        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}
