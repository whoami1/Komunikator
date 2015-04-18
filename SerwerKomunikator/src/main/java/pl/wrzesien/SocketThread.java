package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.LoginRequest;
import pl.entities.request.RegisterRequest;
import pl.entities.request.TestowaWiadomoscRequest;
import pl.entities.response.LoginResponse;
import pl.entities.response.RegistrationResponse;
import pl.entities.response.TestowaWiadomoscResponse;
import pl.entities.response.UserListResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class SocketThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketThread.class);
    private Map<User, Socket> onlineUsersToSocketMap;
    private Socket socket;

    public SocketThread(Map<User, Socket> onlineUsersToSocketMap, Socket socket) {
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
                    System.out.println(loginRequest.toString());
                    boolean success = us.checkCredentials(loginRequest.getLogin(), loginRequest.getPassword());
                    if (success) {
                        user = new User(loginRequest.getLogin());
                        onlineUsersToSocketMap.put(user, socket);
                        oos.writeObject(new LoginResponse(success));
                        ArrayList<User> userList = new ArrayList<>();
                        userList.addAll(onlineUsersToSocketMap.keySet());
                        oos.writeObject(new UserListResponse(userList));
                        LOGGER.info(log("Zalogowano uzytkownika: " + loginRequest.getLogin()));
                    } else {
                        oos.writeObject(new LoginResponse(success));
                        LOGGER.info(log("Nieprawidlowy login lub haslo - rozlaczam z " + socket.getRemoteSocketAddress()));
                        break;
                    }
                } else if (obj instanceof RegisterRequest) {
                    RegisterRequest registerRequest = (RegisterRequest) obj;
                    System.out.println(registerRequest.toString());
                    boolean succes = us.checkIfLoginExists(registerRequest.getLogin());

                    if (succes) {
                        oos.writeObject(new RegistrationResponse(succes));
                        LOGGER.info(log("Uzytkownik o podanym loginie: " + registerRequest.getLogin() + " juz istnieje - rozlaczam z " + socket.getRemoteSocketAddress()));
                        break;
                    } else {
                        oos.writeObject(new RegistrationResponse(succes));
                        us.newUser(registerRequest.getLogin(), registerRequest.getPassword());
                        LOGGER.info(log("Zarejestrowano uzytkownika o loginie: " + registerRequest.getLogin() + " - rozlaczam z " + socket.getRemoteSocketAddress()));
                        break;
                    }
                } else if (obj instanceof TestowaWiadomoscRequest) {
                    TestowaWiadomoscRequest testowaWiadomoscRequest = (TestowaWiadomoscRequest) obj;
                    System.out.println(testowaWiadomoscRequest.toString());
                    boolean succes = true;
                    String innyUzytkownik = "innyuzytkownik";
                    String text = "Odpowiedz od serwera";
                    Object odpowiedz = new TestowaWiadomoscResponse(succes, innyUzytkownik, text);
                    oos.writeObject(odpowiedz);
                    System.out.println(odpowiedz);
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