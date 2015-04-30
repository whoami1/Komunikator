package pl.entities.request;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-16.
 */
public class MessageRequest implements Serializable {
    private static final long serialVersionUID = 1;

    private String username;
    private String text;

    public MessageRequest(String username, String text) {
        this.username = username;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "pl.entities.request.MessageRequest{" +
                "username='" + username + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
