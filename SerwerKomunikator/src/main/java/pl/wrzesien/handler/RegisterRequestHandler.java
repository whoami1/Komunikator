package pl.wrzesien.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.RegisterRequest;
import pl.entities.response.RegistrationResponse;
import pl.wrzesien.Communication;
import pl.wrzesien.UserInfo;
import pl.wrzesien.UserName;
import pl.wrzesien.UserService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Micha³ Wrzesieñ on 2015-06-09.
 */
public class RegisterRequestHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterRequestHandler.class);

    public void execute(Object obj, UserService userService, ObjectOutputStream oos, Socket socket, Map<String, Communication> allUsersToCommunicationMap, UserName userName) throws IOException
    {
        RegisterRequest registerRequest = (RegisterRequest) obj;
        LOGGER.info(registerRequest.toString());
        boolean succes = userService.checkIfLoginExists(registerRequest.getLogin());

        if (succes)
        {
            oos.writeObject(new RegistrationResponse(succes));
            LOGGER.info("|" + "Port: " + socket.getPort() + "|" + "Uzytkownik o podanym loginie: " + registerRequest.getLogin() + " juz istnieje - rozlaczam z " + socket.getRemoteSocketAddress());
        } else
        {
            UserInfo userInfo = new UserInfo(registerRequest.getLogin(), false);
            Communication communication = new Communication(new ArrayList<>(), userInfo);
            allUsersToCommunicationMap.put(registerRequest.getLogin(), communication);
            oos.writeObject(new RegistrationResponse(succes));
            userService.newUser(registerRequest.getLogin(), registerRequest.getPassword());
            LOGGER.info("|" + "Port: " + socket.getPort() + "|" + "Zarejestrowano uzytkownika o loginie: " + registerRequest.getLogin() + " - rozlaczam z " + socket.getRemoteSocketAddress());
        }
    }
}
