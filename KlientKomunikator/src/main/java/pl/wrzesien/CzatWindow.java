package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Michał Wrzesień on 2015-03-24.
 */
public class CzatWindow extends JFrame
{
    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.CzatWindow.class);
    private JPanel czatWindow;
    private JTextArea txtRozmowaWOknieCzatu;
    private JTextField txtWiadomosc;
    private JButton wyslijButton;
    private JLabel lblUruchomionyUzytkownik;
    private JButton wyslijPlikButton;
    private JButton doladujOstatniaRozmowaButton;
    private String odbiorca;
    private Client client;
    private String mojNick;
    private Date dNow;

    public CzatWindow(String odbiorca, Client client, String mojNick)
    {
        this.client = client;
        this.odbiorca = odbiorca;
        this.mojNick = mojNick;

        txtRozmowaWOknieCzatu.setEditable(false);
        lblUruchomionyUzytkownik.setText("Użytkownik: " + "\"" + mojNick + "\"" + " rozmowa z: " + "\"" + odbiorca + "\"");

        JFileChooser chooser = new JFileChooser();
        LokalizujNapisyPL(chooser);

        wyslijButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setTxtRozmowaWOknieCzatu(mojNick, txtWiadomosc.getText());
                client.wyslanieWiadomosciNaSerwer(odbiorca, txtWiadomosc.getText());
                txtWiadomosc.setText(null);
            }
        });

        wyslijPlikButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                chooser.setMultiSelectionEnabled(false);

                FileNameExtensionFilter filtr = new FileNameExtensionFilter("Obrazy JPEG & GIF & PNG", "jpg", "gif", "png");
                chooser.setFileFilter(filtr);
                int returnVal = chooser.showOpenDialog(czatWindow);

                File plik = chooser.getSelectedFile();

                try
                {
                    if (returnVal == JFileChooser.APPROVE_OPTION)
                    {
                        splitFile(plik);
                        setTxtRozmowaWOknieCzatu(mojNick, "Wysłano następujący plik: " + plik.getName());
                    }
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        doladujOstatniaRozmowaButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser doladujHistorie = new JFileChooser("archiwum");
                LokalizujNapisyPL(doladujHistorie);
                doladujHistorie.setMultiSelectionEnabled(false);
                Action details = doladujHistorie.getActionMap().get("viewTypeDetails");
                details.actionPerformed(null);

                doladujHistorie.setFileFilter(new FileFilter()
                {
                    @Override
                    public boolean accept(File f)
                    {
                        return (f.isDirectory() || f.getName().startsWith(odbiorca));
                    }

                    @Override
                    public String getDescription()
                    {
                        return null;
                    }
                });

                int returnVal = doladujHistorie.showOpenDialog(czatWindow);

                HistoriaZapisOdczyt historiaZapisOdczyt = new HistoriaZapisOdczyt();

                String name = doladujHistorie.getSelectedFile().getName();

                try
                {
                    if (returnVal == JFileChooser.APPROVE_OPTION)
                    {
                        ArrayList<String> tekstRozmowyTablica = historiaZapisOdczyt.odczytPliku("archiwum" + "\\" + name);
                        for (String tekstRozmowy : tekstRozmowyTablica)
                        {
                            txtRozmowaWOknieCzatu.append("*" + tekstRozmowy);
                        }
                    }
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void splitFile(File f) throws IOException
    {
        int sizeOfFiles = 1024 * 1024;// 1MB
        int last_part_size = (int) (f.length() % sizeOfFiles);
        long length = f.length();
        double a = (double) ((double) length / (double) sizeOfFiles);
        int parts = (int) Math.ceil(a);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f)))
        {//try-with-resources to ensure closing stream
            String filename = f.getName();

            for (int i = 0; i < parts; i++)
            {
                int current_part_size = sizeOfFiles;
                if (i == parts - 1)
                    current_part_size = last_part_size;
                byte[] buffer = new byte[current_part_size];
                bis.read(buffer, 0, current_part_size);
                client.wyslaniePlikuNaSerwer(odbiorca, buffer, filename + "." + String.format("%03d", i));
            }
        }
    }

    public void setTxtRozmowaWOknieCzatu(String nadawca, String txtRozmowaWOknieCzatu)
    {
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy :: kk:mm:ss");
        dNow = new Date();
        String data = ft.format(dNow);

        String rozmowa = "     " + nadawca + " :: " + data + "\n" + txtRozmowaWOknieCzatu + "\n";
        this.txtRozmowaWOknieCzatu.append(rozmowa);
        zapisHistoriiRozmowy(rozmowa);
    }

    public void zapisHistoriiRozmowy(String text)
    {
        HistoriaZapisOdczyt historiaZapisOdczyt = new HistoriaZapisOdczyt();

        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        dNow = new Date();
        String data = ft.format(dNow);

        String path = "archiwum";
        String nazwaPliku = odbiorca + "_" + data + ".txt";

        if (historiaZapisOdczyt.czyFolderIstnieje(path))
        {
            historiaZapisOdczyt.czyPlikIstnieje(nazwaPliku);
            try
            {
                historiaZapisOdczyt.zapisPliku(nazwaPliku, text);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } else
        {
            historiaZapisOdczyt.czyPlikIstnieje(nazwaPliku);
            try
            {
                historiaZapisOdczyt.zapisPliku(nazwaPliku, text);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public JTextArea getTxtRozmowaWOknieCzatu()
    {
        return txtRozmowaWOknieCzatu;
    }

    public void showWindow()
    {
        setTitle("Komunikator - Czat - " + odbiorca);
        setContentPane(czatWindow);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        txtWiadomosc.requestFocusInWindow();
        czatWindow.getRootPane().setDefaultButton(wyslijButton);
    }

    public void closeWindow()
    {
        setVisible(false);
        dispose();
    }

    public void LokalizujNapisyPL(JComponent komponent)
    {
        UIManager.put("FileChooser.lookInLabelText", "Szukaj w");
        UIManager.put("FileChooser.lookInLabelMnemonic", "" + KeyEvent.VK_W);

        UIManager.put("FileChooser.saveInLabelText", "Zapisz w");
        UIManager.put("FileChooser.saveInLabelMnemonic", "" + KeyEvent.VK_W);

        UIManager.put("FileChooser.fileNameLabelText", "Nazwa pliku:");
        UIManager.put("FileChooser.fileNameLabelMnemonic", "" + KeyEvent.VK_N);

        UIManager.put("FileChooser.filesOfTypeLabelText", "Pliki typu:");
        UIManager.put("FileChooser.filesOfTypeLabelMnemonic", "" + KeyEvent.VK_P);

        UIManager.put("FileChooser.upFolderToolTipText", "Poziom wyżej");
        UIManager.put("FileChooser.homeFolderToolTipText", "Pulpit");
        UIManager.put("FileChooser.newFolderToolTipText", "Nowy katalog");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Szczegóły");

        UIManager.put("FileChooser.fileNameHeaderText", "Nazwa");
        UIManager.put("FileChooser.fileSizeHeaderText", "Rozmiar");
        UIManager.put("FileChooser.fileTypeHeaderText", "Typ");
        UIManager.put("FileChooser.fileDateHeaderText", "Modyfikacja");
        UIManager.put("FileChooser.fileAttrHeaderText", "Atrybuty");

        UIManager.put("FileChooser.newFolderErrorText", "Błąd podczas tworzenia katalogu");

        UIManager.put("FileChooser.saveButtonText", "Zapisz");
        UIManager.put("FileChooser.saveButtonMnemonic", "" + KeyEvent.VK_Z);

        UIManager.put("FileChooser.openButtonText", "Otwórz");
        UIManager.put("FileChooser.openButtonMnemonic", "" + KeyEvent.VK_O);

        UIManager.put("FileChooser.cancelButtonText", "Anuluj");
        UIManager.put("FileChooser.openButtonMnemonic", "" + KeyEvent.VK_R);

        UIManager.put("FileChooser.openDialogTitleText", "Otwieranie");
        UIManager.put("FileChooser.saveDialogTitleText", "Zapisywanie");

        UIManager.put("FileChooser.saveButtonToolTipText", "Zapisanie pliku");
        UIManager.put("FileChooser.openButtonToolTipText", "Otwarcie pliku");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Anuluj");
        UIManager.put("FileChooser.acceptAllFileFilterText", "Wszystkie pliki");

        UIManager.put("FileChooser.directoryOpenButtonText", "Otwórz katalog");
        UIManager.put("FileChooser.directoryOpenButtonToolTipText", "Otwiera katalog");

        UIManager.put("FileChooser.foldersLabelText", "Nazwa folderu: ");
        UIManager.put("FileChooser.pathLabelText", "Ścieżka: ");
        UIManager.put("FileChooser.directoryDescriptionText", "Scieżka katalogu, opis");
        UIManager.put("FileChooser.foldersLabelText", "Foldery");
        UIManager.put("FileChooser.newFolderAccessibleName", "Nowy folder");
        UIManager.put("FileChooser.newFolderToolTipText", "Nowy folder");
        UIManager.put("FileChooser.other.newFolder", "Nowy folder");
        UIManager.put("FileChooser.other.newFolder.subsequent", "Nowy folder");
        UIManager.put("FileChooser.win32.newFolder", "Nowy folder");
        UIManager.put("FileChooser.win32.newFolder.subsequent", "Nowy folder");

        SwingUtilities.updateComponentTreeUI(komponent);
    }
}
