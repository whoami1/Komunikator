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
    private Map<User, Communication> onlineUsersToSocketMap;
    private Socket socket;

    public SocketThread(Map<User, Communication> onlineUsersToSocketMap, Socket socket) {
        this.onlineUsersToSocketMap = onlineUsersToSocketMap;
        this.socket = socket;
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
            UserService us = new UserService();
            LOGGER.info(log("Polaczono z" + socket.getRemoteSocketAddress()));

            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());

            Object obj;

            while ((obj = ois.readObject()) != null) {
                if (obj instanceof LoginRequest) {
                    LoginRequest loginRequest = (LoginRequest) obj;
                    LOGGER.info(log(loginRequest.toString()));
                    boolean success = us.checkCredentials(loginRequest.getLogin(), loginRequest.getPassword());
                    if (success) {
                        user = new User(loginRequest.getLogin());
                        Communication communication = new Communication(socket, new ArrayList<>());
                        onlineUsersToSocketMap.put(user, communication);
                        oos.writeObject(new LoginResponse(success));

                        ArrayList<User> userList = new ArrayList<>();
                        userList.addAll(onlineUsersToSocketMap.keySet());
                        oos.writeObject(new UserListResponse(userList));

                        //TUTAJ
/*                        ArrayList<User> allUsersList = new ArrayList<>();
                        allUsersList.addAll(us.showAllUsers());
                        oos.writeObject(new AllUsersListResponse(allUsersList));*/

                        LOGGER.info(log("Zalogowano uzytkownika: " + loginRequest.getLogin()));
                    } else {
                        oos.writeObject(new LoginResponse(success));
                        LOGGER.info(log("Nieprawidlowy login lub haslo - rozlaczam z " + socket.getRemoteSocketAddress()));
                    }
                } else if (obj instanceof RegisterRequest) {
                    RegisterRequest registerRequest = (RegisterRequest) obj;
                    System.out.println(registerRequest.toString());
                    boolean succes = us.checkIfLoginExists(registerRequest.getLogin());

                    if (succes) {
                        oos.writeObject(new RegistrationResponse(succes));
                        LOGGER.info(log("Uzytkownik o podanym loginie: " + registerRequest.getLogin() + " juz istnieje - rozlaczam z " + socket.getRemoteSocketAddress()));
                    } else {
                        oos.writeObject(new RegistrationResponse(succes));
                        us.newUser(registerRequest.getLogin(), registerRequest.getPassword());
                        LOGGER.info(log("Zarejestrowano uzytkownika o loginie: " + registerRequest.getLogin() + " - rozlaczam z " + socket.getRemoteSocketAddress()));
                    }
                } else if (obj instanceof MessageRequest) {
                    LOGGER.info(onlineUsersToSocketMap.toString());
                    MessageRequest messageRequest = (MessageRequest) obj;
                    onlineUsersToSocketMap.get(new User(messageRequest.getUsername())).getListOfMessageResponse().add(new MessageResponse(user, messageRequest.getText()));
                } else if (obj instanceof AllMesageRequest) {
                    Communication communication = onlineUsersToSocketMap.get(user);
                    List<MessageResponse> listOfMessageResponse = communication.getListOfMessageResponse();
                    oos.writeObject(new AllMessageResponse(listOfMessageResponse));
                    onlineUsersToSocketMap.put(user, new Communication(communication.getSocket(), new ArrayList<>()));
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
                onlineUsersToSocketMap.remove(user);
                LOGGER.info("Uzytkownik: " + user.getUserNick() + " rozlaczyl sie");
            }
        }
    }
}