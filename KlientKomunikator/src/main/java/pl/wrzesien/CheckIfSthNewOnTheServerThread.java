package pl.wrzesien;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.response.FileMessageResponse;
import pl.entities.response.MessageResponse;
import pl.entities.response.TextMessageResponse;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Micha� Wrzesie� on 2015-05-06.
 */
public class CheckIfSthNewOnTheServerThread implements Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.CheckIfSthNewOnTheServerThread.class);
    // liczba milisekund pauzy (1000 ms czyli 1 sekunda)
    public static final int PAUZA_MS = 1000;
    private Client client;
    private String myNickname;
    private HashMap<String, CzatWindow> odbiorcaDoCzatWindowMap;
    private UserListListner userListListner;

    // konstruktor klasy
    public CheckIfSthNewOnTheServerThread(Client client, String myNickname, HashMap<String, CzatWindow> odbiorcaDoCzatWindowMap)
    {
        this.client = client;
        this.myNickname = myNickname;
        this.odbiorcaDoCzatWindowMap = odbiorcaDoCzatWindowMap;
    }

    public void odbierzWiadomosc()
    {
        List<MessageResponse> messageResponses = client.odebranieWiadomosciZSerwera();

        CzatWindow czatWindow = null;
        boolean odebranoPlik = false;
        String odebranoPlik_nazwa = "";

        for (MessageResponse messageResponse : messageResponses)
        {
            if (messageResponse instanceof TextMessageResponse)
            {
                String odbiorca = messageResponse.getRecipiant();

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
                } else
                {
                    czatWindow = odbiorcaDoCzatWindowMap.get(odbiorca);
                }
                czatWindow.setTxtRozmowaWOknieCzatu(odbiorca, ((TextMessageResponse) messageResponse).getTextMessage());
            } else if (messageResponse instanceof FileMessageResponse)
            {
                odebranoPlik = true;
                String odbiorca = messageResponse.getRecipiant();
                byte[] plik = ((FileMessageResponse) messageResponse).getFile();
                String filename = ((FileMessageResponse) messageResponse).getFilename();
                odebranoPlik_nazwa = filename;

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
                } else
                {
                    czatWindow = odbiorcaDoCzatWindowMap.get(odbiorca);
                }

                try
                {
                    File pathname = new File("odebrane");
                    if (!pathname.isDirectory())
                    {
                        pathname.mkdir();
                        LOGGER.info("Folder " + pathname + " zostal utworzony");
                    }
                    //File tmp = File.createTempFile(filename + "-", ",tmp", pathname);
                    File tmp = new File("odebrane" + "\\" + filename);
                    FileUtils.writeByteArrayToFile(tmp, plik);
                    czatWindow.setTxtRozmowaWOknieCzatu(odbiorca, "Odebrano plik: " + tmp);

                    LOGGER.info("Odebrano plik: " + tmp);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        if(odebranoPlik)
        {
            String[] filesplit = odebranoPlik_nazwa.split("\\.");
            String filename = "";
            String strfileparts = filesplit[filesplit.length-1];
            int fileparts = Integer.parseInt(strfileparts);
            for(int i=0;i<filesplit.length-1;i++)
            {
                if (i == filesplit.length - 2)
                    filename += filesplit[i];
                else
                    filename += filesplit[i] + ".";
            }
            File into = new File("odebrane\\" + filename);
            List<File> files = new ArrayList<>();
            for(int i=0;i<fileparts+1;i++)
                files.add(new File("odebrane\\" + filename + "." + String.format("%03d", i)));
            try
            {
                try (BufferedOutputStream mergingStream = new BufferedOutputStream(new FileOutputStream(into)))
                {
                    for (File f : files) {
                        try
                        {
                            Files.copy(f.toPath(), mergingStream);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            //remove parts
        }
    }

    public void addUserListListener(UserListListner userListListner)
    {
        this.userListListner = userListListner;
    }

    public void aktualizujStatusUzytkownikow()
    {
        List<UserInfo> userInfos = client.listaWszystkichUzytkownikowRequest();
        userListListner.onUserList(userInfos);
    }

    // metoda wywolana po starcie watku
    public void run()
    {
        while (!client.disconnected)
        {
            odbierzWiadomosc();
            aktualizujStatusUzytkownikow();
            try
            {
                // wstrzymujemy dzialanie watku na 1 sekunde
                Thread.sleep(PAUZA_MS);
            } catch (InterruptedException e)
            {
                break;
            }
        }
    }
}