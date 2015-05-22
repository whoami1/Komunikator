package pl.wrzesien;

import pl.entities.response.Message;
import pl.entities.response.MessageResponse;

import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class Communication {
    private List<Message> listOfMessageResponse;

    private UserInfo userInfo;

    public Communication(List<Message> listOfMessageResponse, UserInfo userInfo) {
        this.listOfMessageResponse = listOfMessageResponse;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public List<Message> getListOfMessageResponse() {
        return listOfMessageResponse;
    }

    public void setListOfMessageResponse(List<Message> listOfMessageResponse) {
        this.listOfMessageResponse = listOfMessageResponse;
    }
}
