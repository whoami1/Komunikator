package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.entities.response.MessageResponse;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-22.
 */
public class KontaktyWindow extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.KontaktyWindow.class);

    private JButton historiaButton;
    private JButton dodajKontaktButton;
    private JTable uzytkownicy;
    private JPanel kontaktyWindow;
    private JButton oAplikacjiButton;
    private JLabel lblUruchomionyUzytkownik;
    private JButton odbierzButton;
    private JButton wylogujButton;

    private String mojNick;
    private String nadawca = null;
    private CzatWindow czatWindow;
    private boolean oknoCzatuZostaloOtwarte = false;

    private Client client;
    private List<UserInfo> allUsers;

    public KontaktyWindow(Client client, String mojNick, List<UserInfo> allUsers) {
        this.mojNick = mojNick;
        this.client = client;
        this.allUsers = allUsers;

        lblUruchomionyUzytkownik.setText("Konto użytkownika: " + mojNick);

        //new Thread(new CheckIfSthNewOnTheServerThread(this)).start();

        wylogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    client.closeConnection();
                    LOGGER.info("Połączenie z serwerem zostało zakończone, użytkownik: " + "*" + mojNick + "*" + " wylogował się...");
                }
                closeKontaktyWindow();
                MainWindow mainWindow = new MainWindow();
                mainWindow.showMainWindow();
            }
        });

        oAplikacjiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OAplikacjiDialog.openOAplikacjiDialog();
            }
        });

        dodajKontaktButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DodajKontaktDialog.openDodajKontaktyDialog();
            }
        });

        odbierzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                przyciskOdbierz();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client != null) {
                    client.closeConnection();
                    LOGGER.info("Połączenie z serwerem zostało zakończone...");
                }
            }
        });
    }

    public void przyciskOdbierz() {
        List<MessageResponse> messageResponses = client.odebranieWiadomosciZSerwera();

        if (!messageResponses.isEmpty()) {
            MessageResponse userNick = messageResponses.get(0);
            nadawca = userNick.getUserInfo();

            if (oknoCzatuZostaloOtwarte == false) {
                czatWindow = new CzatWindow(nadawca, client, mojNick);
                czatWindow.showCzatWindow();
                oknoCzatuZostaloOtwarte = true;
            }

            if (oknoCzatuZostaloOtwarte == true) {
                for (int i = 0; i < messageResponses.size(); i++) {
                    MessageResponse messageResponse = messageResponses.get(i);
                    nadawca = messageResponse.getUserInfo();
                    czatWindow.setTxtRozmowaWOknieCzatu(nadawca, messageResponse.getMessage());
                }
            }
        }
    }

    private void createUIComponents() {

        String columnNames[] = new String[]{"Nick", "Status"};

        Object[][] data = new Object[allUsers.size()][2];
        for (int i = 0; i < allUsers.size(); i++) {
            data[i][0] = allUsers.get(i).getUserNick();
            data[i][1] = allUsers.get(i).getUserStatus();

            //Podmiana statusu z boolean na tekstowy
            if (data[i][1].equals(true)) {
                data[i][1] = "dostępny";
            } else {
                data[i][1] = "niedostępny";
            }
        }

        uzytkownicy = new JTable(data, columnNames) {
            public boolean isCellEditable(int data, int columnNames) {
                return false;
            } //zablokowanie edycji komorek tabeli
        };

        uzytkownicy.setRowSelectionAllowed(true);

        // uzytkownicy.setFocusable(false);
        uzytkownicy.setFillsViewportHeight(true);
        uzytkownicy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        uzytkownicy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String kontaktZListyUzytkownikow = (String) uzytkownicy.getValueAt(uzytkownicy.getSelectedRow(), 0);
                    System.out.println("***************" + kontaktZListyUzytkownikow);
                    czatWindow = new CzatWindow(kontaktZListyUzytkownikow, client, mojNick);
                    czatWindow.showCzatWindow();
                    oknoCzatuZostaloOtwarte = true;
                }
            }
        });
    }

    public void showKontaktyWindow() {
        setTitle("Komunikator - Kontakty");
        //JPanel kontaktyWindow = new KontaktyWindow(client, mojNick, allUsers).kontaktyWindow;
        setContentPane(kontaktyWindow);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void closeKontaktyWindow() {
        this.setVisible(false);
        this.dispose();
    }
}