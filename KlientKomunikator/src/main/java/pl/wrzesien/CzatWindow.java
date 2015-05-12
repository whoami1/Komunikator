package pl.wrzesien;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Michał Wrzesień on 2015-03-24.
 */
public class CzatWindow extends JFrame {
    private JPanel czatWindow;
    private JTextArea txtRozmowaWOknieCzatu;
    private JTextField txtWiadomosc;
    private JButton wyslijButton;
    private JLabel lblUruchomionyUzytkownik;
    private JButton wyslijPlikButton;
    private String odbiorca;
    private Client client;
    private String nadawca;

    public CzatWindow(String odbiorca, Client client, String nadawca) {
        this.client = client;
        this.odbiorca = odbiorca;
        this.nadawca = nadawca;

        txtRozmowaWOknieCzatu.setEditable(false);
        lblUruchomionyUzytkownik.setText("Użytkownik: " + "\"" + nadawca + "\"" + " rozmowa z: " + "\"" + odbiorca + "\"");

        JFileChooser chooser = new JFileChooser();
        LokalizujNapisyPL(chooser);

        wyslijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //txtRozmowaWOknieCzatu.append(userInfo.getUserNick()+ ": " + txtWiadomosc.getText() + "\n");
                //txtRozmowaWOknieCzatu.append(nadawca + ": " + txtWiadomosc.getText() + "\n");
                setTxtRozmowaWOknieCzatu(nadawca, txtWiadomosc.getText());
                client.wyslanieWiadomosciNaSerwer(odbiorca, txtWiadomosc.getText());
                txtWiadomosc.setText(null);
            }
        });

        wyslijPlikButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                chooser.setMultiSelectionEnabled(true);
                Action details = chooser.getActionMap().get("viewTypeDetails");
                details.actionPerformed(null);

                FileNameExtensionFilter filtr = new FileNameExtensionFilter("Obrazy JPEG & GIF & PNG", "jpg", "gif", "png");
                chooser.setFileFilter(filtr);
                int returnVal = chooser.showOpenDialog(czatWindow);

                File[] pliki = chooser.getSelectedFiles();

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    for (File plik : pliki)
                        System.out.println("Wybrano plik: " + plik.getName());
                }
            }
        });
    }

    public void setTxtRozmowaWOknieCzatu(String nadawca, String txtRozmowaWOknieCzatu) {
        String rozmowa = nadawca + ": " + txtRozmowaWOknieCzatu + "\n";
        this.txtRozmowaWOknieCzatu.append(rozmowa);

        zapisHistoriiRozmowy(rozmowa);
    }

    public void zapisHistoriiRozmowy(String text) {
        HistoriaZapisOdczyt historiaZapisOdczyt = new HistoriaZapisOdczyt();

        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy");
        String data = ft.format(dNow);

        String path = "archiwum";
        String nazwaPliku = odbiorca + "_" + data + ".txt";

        if (historiaZapisOdczyt.czyFolderIstnieje(path)) ;
        {
            historiaZapisOdczyt.czyPlikIstnieje(nazwaPliku);
            try {
                historiaZapisOdczyt.zapisPliku(nazwaPliku, text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public JTextArea getTxtRozmowaWOknieCzatu() {
        return txtRozmowaWOknieCzatu;
    }

    public void showWindow() {
        setTitle("Komunikator - Czat - " + odbiorca);
        setContentPane(czatWindow);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        txtWiadomosc.requestFocusInWindow();
        czatWindow.getRootPane().setDefaultButton(wyslijButton);
    }

    public void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }

    public void LokalizujNapisyPL(JComponent komponent) {
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
