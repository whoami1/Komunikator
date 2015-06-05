package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.request.*;
import pl.entities.response.*;

import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-31.
 */
public class Client
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private String serverIp;
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    public boolean disconnected;

    public Client()
    {
        disconnected = false;
    }

    public String log(String text)
    {
        return "|" + "Port: " + client.getPort() + "|" + text;
    }

    private Object read()
    {
        Object obj;
        try
        {
            while ((obj = in.readObject()) != null)
            {
                return obj;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public boolean login(String userLogin, String userPassword)
    {
        try
        {
            out.writeObject(new LoginRequest(userLogin, userPassword));
            LoginResponse loginResponse = (LoginResponse) read();
            return loginResponse.isSucces();
        } catch (SocketException ee)
        {
            LOGGER.info(ee.toString() + " - logowanie nie powiodło się, połączenie z serwerem zerwane...");
            JOptionPane.showMessageDialog(new JWindow(), "Logowanie nie powiodło się, prawdopodobnie serwer " +
                    "został wyłączony, spróbuj ponownie później...", "Błąd serwera", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String userLogin, String userPassword)
    {
        try
        {
            out.writeObject(new RegisterRequest(userLogin, userPassword));
            RegistrationResponse registrationResponse = (RegistrationResponse) read();
            return registrationResponse.isSucces();
        } catch (SocketException ee)
        {
            LOGGER.info(ee.toString() + " - rejestracja nie powiodła się, połączenie z serwerem zerwane...");
            JOptionPane.showMessageDialog(new JWindow(), "Rejestracja nie powiodła się, prawdopodobnie serwer " +
                    "został wyłączony, spróbuj ponownie później...", "Błąd serwera", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return true; //w wypadku zerwania połączenia zawsze rejestracja nie powiedzie się
    }

    public void wyslanieWiadomosciNaSerwer(String userLogin, String text)
    {
        try
        {
            out.writeObject(new SendMessageRequest(userLogin, text));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<Message> odebranieWiadomosciZSerwera()
    {
        try
        {
            out.writeObject(new AllMesageRequest());
            AllMessageResponse allMessageResponse = (AllMessageResponse) read();
            LOGGER.info(allMessageResponse.toString());
            return allMessageResponse.getMessageResponseList();
        } catch (SocketException ee)
        {
            LOGGER.info(ee.toString() + " - pobranie listy wiadomości nie powiodło się, połączenie z serwerem zerwane...");
            JOptionPane.showMessageDialog(new JWindow(), "Pobranie listy wiadomości nie powiodło się, prawdopodobnie serwer " +
                    "został wyłączony, spróbuj ponownie później...", "Błąd serwera", JOptionPane.ERROR_MESSAGE);
            disconnected = true;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<UserInfo> listaWszystkichUzytkownikowRequest()
    {
        try
        {
            out.writeObject(new AllUsersListRequest());
            AllUsersListResponse allUsersListResponse = (AllUsersListResponse) read();
            LOGGER.info(allUsersListResponse.toString());
            return allUsersListResponse.getAllUsersList();
        } catch (SocketException ee)
        {
            LOGGER.info(ee.toString() + " - pobranie listy znajomych nie powiodło się, połączenie z serwerem zostało zerwane...");
            JOptionPane.showMessageDialog(new JWindow(), "Pobranie listy znajomych nie powiodło się, prawdopodobnie serwer " +
                    "został wyłączony, spróbuj ponownie później...", "Błąd serwera", JOptionPane.ERROR_MESSAGE);
            disconnected = true;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean wyslaniePlikuNaSerwer(String username, byte[] plik, String filename)
    {
        try
        {
            out.writeObject(new FileRequest(username, plik, filename));
            out.flush();
            return true;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public boolean connect(String serverIp)
    {

        int port = 6066;
        this.serverIp = serverIp;

        try
        {
            client = new Socket(serverIp, port);
            LOGGER.info(log("Łączenie z " + serverIp + " na porcie " + port));
            LOGGER.info(log("Połączono z " + client.getRemoteSocketAddress()));
            OutputStream outToServer = client.getOutputStream();
            out = new ObjectOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            in = new ObjectInputStream(inFromServer);
            return true;
        } catch (ConnectException ee)
        {
            LOGGER.info(ee.toString());
            return false;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection()
    {
        if (out != null)
        {
            try
            {
                out.close();
            } catch (SocketException ee)
            {
                LOGGER.info(out + ee.toString());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (in != null)
        {
            try
            {
                in.close();
            } catch (SocketException ee)
            {
                LOGGER.info(in + ee.toString());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            if (client != null)
            {
                client.close();
            }
        } catch (SocketException ee)
        {
            LOGGER.info(client + " " + ee.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        disconnected = true;
    }
}
