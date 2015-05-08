package pl.wrzesien;

import pl.entities.response.MessageResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-06.
 */
public class CheckIfSthNewOnTheServerThread implements Runnable {
    // liczba milisekund pauzy (1000 ms czyli 1 sekunda)
    public static final int PAUZA_MS = 1000;
    private Client client;
    private String myNickname;
    private HashMap<String,CzatWindow> odbiorcaDoCzatWindowMap;

    // konstruktor klasy
    public CheckIfSthNewOnTheServerThread(Client client, String myNickname, HashMap<String,CzatWindow> odbiorcaDoCzatWindowMap) {
        this.client = client;
        this.myNickname = myNickname;
        this.odbiorcaDoCzatWindowMap = odbiorcaDoCzatWindowMap;
    }

    public void odbierzWiadomosc() {
        List<MessageResponse> messageResponses = client.odebranieWiadomosciZSerwera();

        for (MessageResponse messageResponse : messageResponses) {
            String odbiorca = messageResponse.getUserInfo();
            CzatWindow czatWindow = null;
            if (odbiorcaDoCzatWindowMap.get(odbiorca) == null)
            {
                czatWindow = new CzatWindow(odbiorca,client,myNickname);
                odbiorcaDoCzatWindowMap.put(odbiorca, czatWindow);
                czatWindow.showWindow();
            }
            else
            {
                czatWindow = odbiorcaDoCzatWindowMap.get(odbiorca);
            }
            czatWindow.setTxtRozmowaWOknieCzatu(odbiorca, messageResponse.getMessage());
        }
    }

    public void aktualizujStatusUzytkownikow()
    {
        client.listaWszystkichUzytkownikowRequest();
    }

    // metoda wywo³ana po starcie w¹tku
    public void run() {
        // dopóki zmienna watek wskazuje na bie¿¹cy w¹tek
        while (true) {
            odbierzWiadomosc();
            aktualizujStatusUzytkownikow();
            try {
                // wstrzymujemy dzia³anie w¹tku na 1 sekundê
                Thread.sleep(PAUZA_MS);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}