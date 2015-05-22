package pl.entities.response;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-22.
 */
public class TextMessage implements Message {
    private String textMessage;
    private String recipient;

    public TextMessage(String recipient, String textMessage) {
        this.recipient = recipient;
        this.textMessage = textMessage;
    }

    @Override
    public String getRecipiant() {
        return recipient;
    }

    public String getTextMessage() {
        return textMessage;
    }
}
