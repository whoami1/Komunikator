package pl.wrzesien; /**
 * Created by Michał Wrzesień on 2015-03-24.
 */

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

// nowa klasa Zegar zbudowana w oparciu o klasę JLabel
public class Zegar extends JLabel implements Runnable
{
    // wątek
    private Thread watek;
    private boolean looped;
    // liczba milisekund pauzy (1000 ms czyli 1 sekunda)
    private int pauza = 1000;

    // konstruktor klasy
    public Zegar()
    {
        // wyrównamy napisy do środka
        super("", SwingConstants.CENTER);
        // wybieramy font do wyświetlenia zagara (podajemy nazwę, styl oraz rozmiar)
        setFont (new Font ("Consolas", Font.BOLD, 24));
        // ustalamy kolor tekstu
        setForeground(Color.BLACK);
        // ustawiamy przeźroczystość
        setOpaque(true);
        start();
    }

    // metoda start tworzy i uruchamia wątek zegara
    public void start()
    {
        // jeśli nie ma działającego wątka, utwórz i uruchom nowy
        if (watek == null)
        {
            looped = true;
            watek = new Thread(this);
            watek.start();
        }
    }

    // metoda wywołana po starcie wątku
    public void run()
    {
//        if (watek == Thread.currentThread())
//        {
//            return;
//        }

        // dopóki zmienna watek wskazuje na bieżący wątek
        while (looped)
        {
            // nowy obiekt klasy Date
            Date time = new Date();
            // formatowanie
            DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
            // ustawiamy tekst
            setText(df.format(time));
            try
            {
                // wstrzymujemy działanie wątku na 1 sekundę
                watek.sleep(pauza);
            }
            catch (InterruptedException e)
            {
                break;
            }
        }
    }

    // metoda zatrzymująca zegar (wątek)
    public void stop()
    {
        if (watek == null)
        {
            return;
        }

        looped = false;

        // ustawiamy referencję watek na null
        watek.interrupt();
        try {
            watek.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        watek = null;
    }
}