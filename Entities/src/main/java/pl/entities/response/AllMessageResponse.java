package pl.entities.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class AllMessageResponse implements Serializable
{
    private List<MessageResponse> messageResponseList;

    public AllMessageResponse(List<MessageResponse> messageResponseList)
    {

        this.messageResponseList = messageResponseList;
    }

    public List<MessageResponse> getMessageResponseList()
    {
        return messageResponseList;
    }

    @Override
    public String toString()
    {
        return "AllMessageResponse{" +
                "messageResponseList=" + messageResponseList +
                '}';
    }
}
