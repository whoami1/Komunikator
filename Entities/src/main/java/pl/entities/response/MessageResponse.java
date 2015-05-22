package pl.entities.response;

import pl.wrzesien.UserInfo;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class MessageResponse implements Serializable {
    //private Typ;
    private String deliveryUsername;
    private String message;

    public MessageResponse(String deliveryUsername, String message) { //nadawca

        this.deliveryUsername = deliveryUsername;
        this.message = message;
    }

    public String getUserInfo() {
        return deliveryUsername;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "deliveryUsername=" + deliveryUsername +
                ", message='" + message + '\'' +
                '}';
    }
}