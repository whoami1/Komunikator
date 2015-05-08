package pl.wrzesien;

import pl.entities.response.MessageResponse;

import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-06.
 */
public class CheckIfSthNewOnTheServerThread implements Runnable {
    private KontaktyWindow kontaktyWindow;
    // liczba milisekund pauzy (1000 ms czyli 1 sekunda)
    public static final int PAUZA_MS = 1000;
    private Client client;
    private String myNickname;

    // konstruktor klasy
    public CheckIfSthNewOnTheServerThread(Client client, String myNickname) {
        this.client = client;
        this.myNickname = myNickname;
    }

    public void odbierzWiadomosc() {
        List<MessageResponse> messageResponses = client.odebranieWiadomosciZSerwera();

        for (MessageResponse messageResponse : messageResponses) {
            String odbiorca = messageResponse.getUserInfo();
            CzatWindow czatWindow = new CzatWindow(odbiorca,client,myNickname);
            czatWindow.showWindow();
            czatWindow.setTxtRozmowaWOknieCzatu(odbiorca, messageResponse.getMessage());
        }
    }

    // metoda wywo³ana po starcie w¹tku
    public void run() {
        // dopóki zmienna watek wskazuje na bie¿¹cy w¹tek
        while (true) {
            odbierzWiadomosc();
            try {
                // wstrzymujemy dzia³anie w¹tku na 1 sekundê
                Thread.sleep(PAUZA_MS);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}