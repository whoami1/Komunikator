package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.AllMesageRequest;
import pl.entities.request.LoginRequest;
import pl.entities.request.MessageRequest;
import pl.entities.request.RegisterRequest;
import pl.entities.response.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SocketThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketThread.class);
    private Map<String, Communication> allUsersToCommunicationMap;
    private Socket socket;
    private UserService userService;

    public SocketThread(Map<String, Communication> allUsersToCommunicationMap, Socket socket, UserService userService) {
        this.allUsersToCommunicationMap = allUsersToCommunicationMap;
        this.socket = socket;
        this.userService = userService;
    }

    public String log(String text) {
        return "|" + "Port: " + socket.getPort() + "|" + text;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        User user = null;
        try {

            LOGGER.info(log("Polaczono z" + socket.getRemoteSocketAddress()));

            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            Object obj;

            while ((obj = ois.readObject()) != null) {
                if (obj instanceof LoginRequest) {
                    LoginRequest loginRequest = (LoginRequest) obj;
                    LOGGER.info(log(loginRequest.toString()));
                    boolean success = userService.checkCredentials(loginRequest.getLogin(), loginRequest.getPassword());
                    if (success) {
                        Communication communication = allUsersToCommunicationMap.get(loginRequest.getLogin());
                        UserInfo userStatus = communication.getUserInfo();

                        //setter który zmieni status
                        userStatus.setUserStatus(true);

                        oos.writeObject(new LoginResponse(success));

                        ArrayList<UserInfo> allUsersList = new ArrayList<>();
                        allUsersList.addAll(userService.showAllUsers());
                        oos.writeObject(new AllUsersListResponse(allUsersList));

                        LOGGER.info(log("Zalogowano uzytkownika: " + loginRequest.getLogin()));
                    } else {
                        oos.writeObject(new LoginResponse(success));
                        LOGGER.info(log("Nieprawidlowy login lub haslo - rozlaczam z " + socket.getRemoteSocketAddress()));
                    }
                } else if (obj instanceof RegisterRequest) {
                    RegisterRequest registerRequest = (RegisterRequest) obj;
                    System.out.println(registerRequest.toString());
                    boolean succes = userService.checkIfLoginExists(registerRequest.getLogin());

                    if (succes) {
                        oos.writeObject(new RegistrationResponse(succes));
                        LOGGER.info(log("Uzytkownik o podanym loginie: " + registerRequest.getLogin() + " juz istnieje - rozlaczam z " + socket.getRemoteSocketAddress()));
                    } else {
                        oos.writeObject(new RegistrationResponse(succes));
                        userService.newUser(registerRequest.getLogin(), registerRequest.getPassword());
                        LOGGER.info(log("Zarejestrowano uzytkownika o loginie: " + registerRequest.getLogin() + " - rozlaczam z " + socket.getRemoteSocketAddress()));
                    }
                } else if (obj instanceof MessageRequest) {
                    LOGGER.info(allUsersToCommunicationMap.toString());
                    MessageRequest messageRequest = (MessageRequest) obj;
                    allUsersToCommunicationMap.get(new UserInfo(messageRequest.getUsername())).getListOfMessageResponse().add(new MessageResponse(user, messageRequest.getText()));
                } else if (obj instanceof AllMesageRequest) {
                    Communication communication = allUsersToCommunicationMap.get(user);
                    List<MessageResponse> listOfMessageResponse = communication.getListOfMessageResponse();
                    oos.writeObject(new AllMessageResponse(listOfMessageResponse));
                    allUsersToCommunicationMap.put(user, new Communication(communication.getSocket(), new ArrayList<>(), user));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (user != null) {
                allUsersToCommunicationMap.remove(user);
                LOGGER.info("Uzytkownik: " + user.getUserNick() + " rozlaczyl sie");
            }
        }
    }
}