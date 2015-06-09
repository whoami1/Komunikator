package pl.wrzesien.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.response.AllUsersListResponse;
import pl.wrzesien.Communication;
import pl.wrzesien.UserInfo;
import pl.wrzesien.UserName;
import pl.wrzesien.UserService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Micha³ Wrzesieñ on 2015-06-09.
 */
public class AllUsersListRequestHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AllUsersListRequestHandler.class);

    public void execute(Object obj, UserService userService, ObjectOutputStream oos, Socket socket, Map<String, Communication> allUsersToCommunicationMap, UserName userName) throws IOException
    {
        Collection<Communication> communications = allUsersToCommunicationMap.values();
        List<UserInfo> userInfoList = new ArrayList<>();
        for (Communication c : communications)
        {
            userInfoList.add(c.getUserInfo());
        }

        oos.writeObject(new AllUsersListResponse(userInfoList));
        LOGGER.info(userInfoList.toString());
    }
}
