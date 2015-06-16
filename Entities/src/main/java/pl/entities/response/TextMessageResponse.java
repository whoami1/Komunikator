package pl.entities.response;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-22.
 */
public class TextMessageResponse implements MessageResponse
{
    private String textMessage;
    private String recipient;

    public TextMessageResponse(String recipient, String textMessage)
    {
        this.recipient = recipient;
        this.textMessage = textMessage;
    }

    @Override
    public String getRecipiant()
    {
        return recipient;
    }

    public String getTextMessage()
    {
        return textMessage;
    }
}
