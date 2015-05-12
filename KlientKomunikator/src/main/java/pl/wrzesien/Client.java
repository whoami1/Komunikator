package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.*;
import pl.entities.response.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-31.
 */
public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private String serverIp;
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public boolean disconnected;


    public Client() {
        disconnected = false;
    }

    public String log(String text) {
        return "|" + "Port: " + client.getPort() + "|" + text;
    }

    private Object read() {
        Object obj;
        try {
            //List<Object> ret = new ArrayList<>();


            while ((obj = in.readObject()) != null) {
                return obj;
                /*if(obj instanceof AllUsersListResponse)
                {
                    ret.add(obj);
                }
                else
                    return obj;*/
            }
            //return ret;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserInfo> listaWszystkichUzytkownikow() {
        AllUsersListResponse allUsersListResponse = (AllUsersListResponse) read();
        return allUsersListResponse.getAllUsersList();
    }

    public boolean login(String userLogin, String userPassword) {
        try {
            out.writeObject(new LoginRequest(userLogin, userPassword));
            LoginResponse loginResponse = (LoginResponse) read();
            return loginResponse.isSucces();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String userLogin, String userPassword) {
        try {
            out.writeObject(new RegisterRequest(userLogin, userPassword));
            RegistrationResponse registrationResponse = (RegistrationResponse) read();
            return registrationResponse.isSucces();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true; //w wypadku zerwania połączenia zawsze rejestracja nie powiedzie się
    }

    public void wyslanieWiadomosciNaSerwer(String userLogin, String text) {
        try {
            out.writeObject(new SendMessageRequest(userLogin, text));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MessageResponse> odebranieWiadomosciZSerwera() {
        try {
            out.writeObject(new AllMesageRequest());
            AllMessageResponse allMessageResponse = (AllMessageResponse) read();
            LOGGER.info(allMessageResponse.toString());
            return allMessageResponse.getMessageResponseList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<UserInfo> listaWszystkichUzytkownikowRequest()
    {
        try {
            out.writeObject(new AllUsersListRequest());
            AllUsersListResponse allUsersListResponse = (AllUsersListResponse) read();
            LOGGER.info(allUsersListResponse.toString());
            return allUsersListResponse.getAllUsersList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean connect(String serverIp) {

        int port = 6066;
        this.serverIp = serverIp;

        try {
            client = new Socket(serverIp, port);
            LOGGER.info(log("Łączenie z " + serverIp + " na porcie " + port));
            LOGGER.info(log("Połączono z " + client.getRemoteSocketAddress()));
            OutputStream outToServer = client.getOutputStream();
            out = new ObjectOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            in = new ObjectInputStream(inFromServer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection() {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (client != null)
            {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        disconnected = true;
    }
}
