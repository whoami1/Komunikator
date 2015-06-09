package pl.wrzesien.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.response.AllMessageResponse;
import pl.entities.response.MessageResponse;
import pl.wrzesien.Communication;
import pl.wrzesien.UserName;
import pl.wrzesien.UserService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Micha³ Wrzesieñ on 2015-06-09.
 */
public class AllMessageRequestHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AllMessageRequestHandler.class);

    public void execute(Object obj, UserService userService, ObjectOutputStream oos, Socket socket, Map<String, Communication> allUsersToCommunicationMap, UserName userName) throws IOException
    {
        Communication communication = allUsersToCommunicationMap.get(userName.getValue());
        List<MessageResponse> listOfMessageResponse = communication.getListOfMessageResponse();
        oos.writeObject(new AllMessageResponse(listOfMessageResponse));
        communication.setListOfMessageResponse(new ArrayList<>());
    }
}
