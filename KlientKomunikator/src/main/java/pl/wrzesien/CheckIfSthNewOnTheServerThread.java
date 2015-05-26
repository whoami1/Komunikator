package pl.wrzesien;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.response.FileMessage;
import pl.entities.response.Message;
import pl.entities.response.TextMessage;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Micha� Wrzesie� on 2015-05-06.
 */
public class CheckIfSthNewOnTheServerThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.CheckIfSthNewOnTheServerThread.class);
    // liczba milisekund pauzy (1000 ms czyli 1 sekunda)
    public static final int PAUZA_MS = 1000;
    private Client client;
    private String myNickname;
    private HashMap<String, CzatWindow> odbiorcaDoCzatWindowMap;
    private UserListListner userListListner;

    // konstruktor klasy
    public CheckIfSthNewOnTheServerThread(Client client, String myNickname, HashMap<String, CzatWindow> odbiorcaDoCzatWindowMap) {
        this.client = client;
        this.myNickname = myNickname;
        this.odbiorcaDoCzatWindowMap = odbiorcaDoCzatWindowMap;
    }

    public void odbierzWiadomosc() {
        List<Message> messageResponses = client.odebranieWiadomosciZSerwera();

        CzatWindow czatWindow = null;

        for (Message messageResponse : messageResponses)
        {
            if (messageResponse instanceof TextMessage) {
                String odbiorca = messageResponse.getRecipiant();

                if (odbiorcaDoCzatWindowMap.get(odbiorca) == null)
                {
                    czatWindow = new CzatWindow(odbiorca, client, myNickname);
                    odbiorcaDoCzatWindowMap.put(odbiorca, czatWindow);
                    czatWindow.showWindow();
                    czatWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            odbiorcaDoCzatWindowMap.remove(odbiorca);
                        }
                    });
                } else
                {
                    czatWindow = odbiorcaDoCzatWindowMap.get(odbiorca);
                }
                czatWindow.setTxtRozmowaWOknieCzatu(odbiorca, ((TextMessage) messageResponse).getTextMessage());
            }
            else if (messageResponse instanceof FileMessage)
            {
                //Dodac przesylanie nazwy pliku

                String odbiorca = messageResponse.getRecipiant();
                byte[] plik = ((FileMessage) messageResponse).getFile();

                if (odbiorcaDoCzatWindowMap.get(odbiorca) == null)
                {
                    czatWindow = new CzatWindow(odbiorca, client, myNickname);
                    odbiorcaDoCzatWindowMap.put(odbiorca, czatWindow);
                    czatWindow.showWindow();
                    czatWindow.addWindowListener(new WindowAdapter()
                    {
                        @Override
                        public void windowClosed(WindowEvent e)
                        {
                            odbiorcaDoCzatWindowMap.remove(odbiorca);
                        }
                    });
                }
                else
                {
                    czatWindow = odbiorcaDoCzatWindowMap.get(odbiorca);
                }

                try
                {
                    File tmp = File.createTempFile("temp-file-name", ",tmp");
                    FileUtils.writeByteArrayToFile(tmp, plik);
                    czatWindow.setTxtRozmowaWOknieCzatu(odbiorca,"Odebrano fragmenty pliku: " + tmp);

                    LOGGER.info("Odebrano plik: " + tmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addUserListListener(UserListListner userListListner) {
        this.userListListner = userListListner;
    }

    public void aktualizujStatusUzytkownikow() {
        List<UserInfo> userInfos = client.listaWszystkichUzytkownikowRequest();
        userListListner.onUserList(userInfos);
    }

    // metoda wywo�ana po starcie w�tku
    public void run() {
        // dop�ki zmienna watek wskazuje na bie��cy w�tek
        while (!client.disconnected) {
            odbierzWiadomosc();
            aktualizujStatusUzytkownikow();
            try {
                // wstrzymujemy dzia�anie w�tku na 1 sekund�
                Thread.sleep(PAUZA_MS);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}