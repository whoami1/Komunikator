package pl.wrzesien;

import pl.entities.response.MessageResponse;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-06.
 */
public class CheckIfSthNewOnTheServerThread implements Runnable {
    // w¹tek
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

    // metoda start tworzy i uruchamia w¹tek zegara
    public void start() {
        // jeœli nie ma dzia³aj¹cego w¹tka, utwórz i uruchom nowy
        if (watek == null) {
            looped = true;
            watek = new Thread(this);
            watek.start();
        }
    }

    // metoda wywo³ana po starcie w¹tku
    public void run() {
        // dopóki zmienna watek wskazuje na bie¿¹cy w¹tek
        while (looped) {
            kontaktyWindow.przyciskOdbierz();
            try {
                // wstrzymujemy dzia³anie w¹tku na 1 sekundê
                watek.sleep(pauza);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    // metoda zatrzymuj¹ca w¹tek
    public void stop() {
        if (watek == null) {
            return;
        }

        looped = false;

        // ustawiamy referencjê watek na null
        watek.interrupt();
        try {
            watek.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watek = null;
    }
}