package pl.wrzesien;

import pl.entities.response.MessageResponse;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by Micha� Wrzesie� on 2015-05-06.
 */
public class CheckIfSthNewOnTheServerThread implements Runnable {
    // w�tek
    private Thread watek;
    private boolean looped;
    private KontaktyWindow kontaktyWindow;
    // liczba milisekund pauzy (1000 ms czyli 1 sekunda)
    private int pauza = 1000;

    // konstruktor klasy
    public CheckIfSthNewOnTheServerThread(KontaktyWindow kontaktyWindow) {
        this.kontaktyWindow = kontaktyWindow;
        start();
    }

    // metoda start tworzy i uruchamia w�tek zegara
    public void start() {
        // je�li nie ma dzia�aj�cego w�tka, utw�rz i uruchom nowy
        if (watek == null) {
            looped = true;
            watek = new Thread(this);
            watek.start();
        }
    }

    // metoda wywo�ana po starcie w�tku
    public void run() {
        // dop�ki zmienna watek wskazuje na bie��cy w�tek
        while (looped) {
            kontaktyWindow.przyciskOdbierz();
            try {
                // wstrzymujemy dzia�anie w�tku na 1 sekund�
                watek.sleep(pauza);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // metoda zatrzymuj�ca w�tek
    public void stop() {
        if (watek == null) {
            return;
        }

        looped = false;

        // ustawiamy referencj� watek na null
        watek.interrupt();
        try {
            watek.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watek = null;
    }
}