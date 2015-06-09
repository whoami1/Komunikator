package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
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

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;


        UserName userName = new UserName();
        try {
            LOGGER.info(log("Polaczono z" + socket.getRemoteSocketAddress()));

            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
            MessageDispatcher messageDispatcher = new MessageDispatcher(userService, oos, socket, allUsersToCommunicationMap, userName);
            Object obj;

            while ((obj = ois.readObject()) != null) {
                try {
                    messageDispatcher.dispatch(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (EOFException e) {
            LOGGER.info(e.toString() + " - this input stream reach the end before reading eight bytes.");
        } catch (SocketException e) {
            LOGGER.info(e.toString() + " - klient zostal odlaczony brutalnie (zakoncz proces czy cos w tym stylu).");
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
            if (userName.getValue() != null) {
                Communication userDisconnected = allUsersToCommunicationMap.get(userName.getValue());
                LOGGER.info("Uzytkownik: " + "*" + userName.getValue() + "*" + " rozlaczyl sie");

                Communication communication = new Communication(userDisconnected.getListOfMessageResponse(), new UserInfo(userDisconnected.getUserInfo().getUserNick(), false));
                allUsersToCommunicationMap.put(userName.getValue(), communication);
            }
        }
    }
}