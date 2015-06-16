package pl.wrzesien.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.LoginRequest;
import pl.entities.response.LoginResponse;
import pl.wrzesien.Communication;
import pl.wrzesien.UserInfo;
import pl.wrzesien.UserName;
import pl.wrzesien.UserService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Micha³ Wrzesieñ on 2015-06-09.
 */
public class LoginRequestHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRequestHandler.class);

    public void execute(Object obj, UserService userService, ObjectOutputStream oos, Socket socket, Map<String, Communication> allUsersToCommunicationMap, UserName userName) throws IOException
    {
        LoginRequest loginRequest = (LoginRequest) obj;
        LOGGER.info("Port: " + socket.getPort() + "|" + loginRequest.toString());
        boolean success = userService.checkCredentials(loginRequest.getLogin(), loginRequest.getPassword());
        if (success)
        {
            userName.setValue(loginRequest.getLogin());
            Communication communication = allUsersToCommunicationMap.get(userName.getValue());
            Communication communicationNew = new Communication(communication.getListOfMessageResponse(),
                    new UserInfo(communication.getUserInfo().getUserNick(), true));
            allUsersToCommunicationMap.put(userName.getValue(), communicationNew);

            oos.writeObject(new LoginResponse(success));

            LOGGER.info("Port: " + socket.getPort() + "|" + "Zalogowano uzytkownika: " + loginRequest.getLogin());
        } else
        {
            oos.writeObject(new LoginResponse(success));
            LOGGER.info("Port: " + socket.getPort() + "|" + "Nieprawidlowy login lub haslo - rozlaczam z " +
                    socket.getRemoteSocketAddress());
        }
    }
}
