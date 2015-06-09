package pl.wrzesien.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.FileRequest;
import pl.entities.response.FileMessageResponse;
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
public class FileRequestHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FileRequestHandler.class);

    public void execute(Object obj, UserService userService, ObjectOutputStream oos, Socket socket, Map<String, Communication> allUsersToCommunicationMap, UserName userName) throws IOException
    {
        LOGGER.info(allUsersToCommunicationMap.toString());
        FileRequest fileRequest = (FileRequest) obj;
        Communication communication = allUsersToCommunicationMap.get(fileRequest.getUsername());
        communication.getListOfMessageResponse().add(new FileMessageResponse(userName.getValue(), fileRequest.getFile(), fileRequest.getFilename()));
    }
}
