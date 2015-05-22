package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.*;
import pl.entities.response.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class SocketThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketThread.class);
    private volatile Map<String, Communication> allUsersToCommunicationMap;
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

    public List<UserInfo> allUsers() {
        Collection<Communication> communications = allUsersToCommunicationMap.values();
        List<UserInfo> userInfoList = new ArrayList<>();
        for (Communication c : communications) {
            userInfoList.add(c.getUserInfo());
        }
        return userInfoList;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        String username = null; //uzytkownik danego watku
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
                        username = loginRequest.getLogin();
                        Communication communication = allUsersToCommunicationMap.get(username);
                        Communication communicationNew = new Communication(communication.getListOfMessageResponse(), new UserInfo(communication.getUserInfo().getUserNick(), true));
                        allUsersToCommunicationMap.put(username, communicationNew);

                        oos.writeObject(new LoginResponse(success));

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
                        UserInfo userInfo = new UserInfo(registerRequest.getLogin(), false);
                        Communication communication = new Communication(new ArrayList<>(), userInfo);
                        allUsersToCommunicationMap.put(registerRequest.getLogin(), communication);
                        oos.writeObject(new RegistrationResponse(succes));
                        userService.newUser(registerRequest.getLogin(), registerRequest.getPassword());
                        LOGGER.info(log("Zarejestrowano uzytkownika o loginie: " + registerRequest.getLogin() + " - rozlaczam z " + socket.getRemoteSocketAddress()));
                    }
                } else if (obj instanceof SendMessageRequest) {
                    LOGGER.info(allUsersToCommunicationMap.toString());
                    SendMessageRequest sendMessageRequest = (SendMessageRequest) obj;
                    Communication communication = allUsersToCommunicationMap.get(sendMessageRequest.getUsername());
                    communication.getListOfMessageResponse().add(new TextMessage(username, sendMessageRequest.getText()));
                } else if (obj instanceof AllMesageRequest) {
                    Communication communication = allUsersToCommunicationMap.get(username);
                    List<Message> listOfMessageResponse = communication.getListOfMessageResponse();
                    oos.writeObject(new AllMessageResponse(listOfMessageResponse));
                    communication.setListOfMessageResponse(new ArrayList<>());
                } else if (obj instanceof AllUsersListRequest) {
                    List<UserInfo> userInfos = allUsers();
                    oos.writeObject(new AllUsersListResponse(userInfos));
                    LOGGER.info(userInfos.toString());
                } else if (obj instanceof FileRequest) {
                    //message response
                    FileRequest fileRequest = (FileRequest) obj;
                    byte[] plik = fileRequest.getPlik();
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
            if (username != null) {
                Communication userDisconnected = allUsersToCommunicationMap.get(username);
                LOGGER.info("Uzytkownik: " + "*" + username + "*" + " rozlaczyl sie");

                Communication communication = new Communication(userDisconnected.getListOfMessageResponse(), new UserInfo(userDisconnected.getUserInfo().getUserNick(), false));
                allUsersToCommunicationMap.put(username, communication);
            }
        }
    }
}