package pl.wrzesien.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.SendMessageRequest;
import pl.entities.response.TextMessageResponse;
import pl.wrzesien.Communication;
import pl.wrzesien.UserName;
import pl.wrzesien.UserService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Micha³ Wrzesieñ on 2015-06-09.
 */
public class SendMessageRequestHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SendMessageRequestHandler.class);

    public void execute(Object obj, UserService userService, ObjectOutputStream oos, Socket socket, Map<String, Communication> allUsersToCommunicationMap, UserName userName) throws IOException
    {
        LOGGER.info(allUsersToCommunicationMap.toString());
        SendMessageRequest sendMessageRequest = (SendMessageRequest) obj;
        Communication communication = allUsersToCommunicationMap.get(sendMessageRequest.getUsername());
        communication.getListOfMessageResponse().add(new TextMessageResponse(userName.getValue(), sendMessageRequest.getText()));
    }
}
