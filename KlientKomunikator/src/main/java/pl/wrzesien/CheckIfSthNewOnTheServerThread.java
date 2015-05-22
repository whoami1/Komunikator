package pl.wrzesien;

import pl.entities.response.Message;
import pl.entities.response.TextMessage;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Micha� Wrzesie� on 2015-05-06.
 */
public class CheckIfSthNewOnTheServerThread implements Runnable {
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


        for (Message messageResponse : messageResponses) {
            if (messageResponse instanceof TextMessage) {
                String odbiorca = messageResponse.getRecipiant();
                CzatWindow czatWindow = null;
                if (odbiorcaDoCzatWindowMap.get(odbiorca) == null) {
                    czatWindow = new CzatWindow(odbiorca, client, myNickname);
                    odbiorcaDoCzatWindowMap.put(odbiorca, czatWindow);
                    czatWindow.showWindow();
                    czatWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            odbiorcaDoCzatWindowMap.remove(odbiorca);
                        }
                    });
                } else {
                    czatWindow = odbiorcaDoCzatWindowMap.get(odbiorca);
                }
                czatWindow.setTxtRozmowaWOknieCzatu(odbiorca, ((TextMessage) messageResponse).getTextMessage());
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