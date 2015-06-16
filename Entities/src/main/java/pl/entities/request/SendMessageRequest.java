package pl.entities.request;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-16.
 */
public class SendMessageRequest implements Serializable
{
    private String username;
    private String text;

    public SendMessageRequest(String username, String text)
    {
        this.username = username;
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public String toString()
    {
        return "pl.entities.request.SendMessageRequest{" +
                "username='" + username + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
