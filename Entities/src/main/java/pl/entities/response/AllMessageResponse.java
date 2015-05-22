package pl.entities.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class AllMessageResponse implements Serializable {
    private List<Message> messageResponseList;

    public AllMessageResponse(List<Message> messageResponseList) {

        this.messageResponseList = messageResponseList;
    }

    public List<Message> getMessageResponseList() {
        return messageResponseList;
    }

    @Override
    public String toString() {
        return "AllMessageResponse{" +
                "messageResponseList=" + messageResponseList +
                '}';
    }
}
