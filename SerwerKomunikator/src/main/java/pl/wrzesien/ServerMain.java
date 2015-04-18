package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class ServerMain extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);
    private ServerSocket serverSocket;

    private Map<User, Socket> userSocketMap = new HashMap<>();

    /*private ArrayList<User> userList = new ArrayList<>();

    public void addOnlineUser(User username) {
        userList.add(username);
    }

    public void removeOnlineUser(User username) {
        userList.remove(username);
    }

    public void printOnlineUsers() {
        System.out.println();
        //message.logServerSocket(serverSocket, "Zalogowani uzytkownicy:");
        for (User u : userList) {
            if (userList.size() != 0) {
                System.out.println(u.getUserNick());
            } else {
                //z jakiegos powodu to nie dziala
                System.out.println("Brak");
                //message.logTxt("Brak zalogowanych uzytkownikow");
            }
        }
        System.out.println();
    }*/

    public ServerMain(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(1000000);
    }

    public String log(String text) {
        return "|" + "Port lokalny: " + serverSocket.getLocalPort() + "|" + text;
    }

    public void run() {
        while (true) {
            try {
                LOGGER.info(log("Oczekiwanie na klienta na porcie " + serverSocket.getLocalPort() + "..."));
                Socket server = serverSocket.accept();//nowy watek + przeslanie do niego tego socketa + kontynuacja petli
                new Thread(new SocketThread(userSocketMap, server)).start();
            } catch (SocketTimeoutException s) {
                LOGGER.info(log("Limit czasu socketu zosta³ przekroczony!"));
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) {
        int port = 6066;
        try {
            Thread t = new ServerMain(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}