package pl.wrzesien;

import pl.entities.response.MessageResponse;

import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class Communication
{
    private List<MessageResponse> listOfMessageResponse;

    private UserInfo userInfo;

    public Communication(List<MessageResponse> listOfMessageResponse, UserInfo userInfo)
    {
        this.listOfMessageResponse = listOfMessageResponse;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo()
    {
        return userInfo;
    }

    public List<MessageResponse> getListOfMessageResponse()
    {
        return listOfMessageResponse;
    }

    public void setListOfMessageResponse(List<MessageResponse> listOfMessageResponse)
    {
        this.listOfMessageResponse = listOfMessageResponse;
    }
}
