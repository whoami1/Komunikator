package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-10.
 */
public class HistoriaZapisOdczyt
{

    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.HistoriaZapisOdczyt.class);

    public boolean czyPlikIstnieje(String nazwaPliku)
    {
        File plik = new File(nazwaPliku);
        return plik.exists() && plik.isFile();
    }

    public boolean czyFolderIstnieje(String path)
    {
        File f = new File(path);

        if (!f.isDirectory())
        {
            f.mkdir();
            return true;
        }
        return false;
    }

    public void zapisPliku(String nazwaPliku, String text) throws IOException
    {
        PrintWriter plik = null;
        String path = "archiwum";
        try
        {
            plik = new PrintWriter(new FileWriter(path + "\\" + nazwaPliku, true));
            plik.println(text);
        } finally
        {
            if (plik != null)
            {
                plik.close();
            }
        }
    }

    public ArrayList<String> odczytPliku(String nazwaPliku) throws IOException
    {
        Scanner plik = null;
        ArrayList<String> tekstRozmowy = new ArrayList<>();
        try
        {
            plik = new Scanner(new BufferedReader(new FileReader(nazwaPliku)));
            plik.useDelimiter("\r\n");
            while (plik.hasNext())
            {
                String line = plik.next();
                tekstRozmowy.add(line);
            }
        } finally
        {
            if (plik != null)
            {
                plik.close();
            }
        }
        return tekstRozmowy;
    }
}
