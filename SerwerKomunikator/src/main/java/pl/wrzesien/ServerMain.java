package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerMain extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);
    public static final int SOCKET_PORT = 6066;

    private ServerSocket serverSocket;
    private UserService userService = new UserService();

    private Map<String, Communication> allUsersToCommunicationMap = new HashMap<>();

    public ServerMain(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);

        for (String user : userService.showAllUsers()) {
            LOGGER.info("Zarejestrowani: " + userService.showAllUsers().toString());
            UserInfo userInfo = new UserInfo(user, false);
            allUsersToCommunicationMap.put(user, new Communication(new ArrayList<>(), userInfo));
        }

    }

    public String log(String text) {
        return "|" + "Port lokalny: " + serverSocket.getLocalPort() + "|" + text;
    }

    public void run() {
        while (true) {
            LOGGER.info(log("Oczekiwanie na klienta na porcie " + serverSocket.getLocalPort() + "..."));
            try {
                Socket server = serverSocket.accept();//nowy watek + przeslanie do niego tego socketa + kontynuacja petli
                LOGGER.info(log("Polaczenie z klientem na porcie: " + serverSocket.getLocalPort() + " zostalo zrealizowane..."));
                new Thread(new SocketThread(allUsersToCommunicationMap, server, userService)).start();
            } catch (SocketTimeoutException s) {
                LOGGER.info(log("Limit czasu socketu zostal przekroczony!"));
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        //int port = 6066;
        try {
            Thread t = new ServerMain(SOCKET_PORT);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}