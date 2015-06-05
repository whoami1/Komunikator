package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-22.
 */
public class KontaktyWindow extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.KontaktyWindow.class);

    private JButton historiaButton;
    private JTable uzytkownicy;
    private JPanel kontaktyWindow;
    private JButton oAplikacjiButton;
    private JLabel lblUruchomionyUzytkownik;
    private JButton wylogujButton;

    private String mojNick;
    private CzatWindow czatWindow;
    private HistoriaWindow historiaWindow;

    private Client client;
    private List<UserInfo> allUsers;
    private HashMap<String, CzatWindow> odbiorcaDoCzatWindowMap;

    public KontaktyWindow(Client client, String mojNick, List<UserInfo> allUsers) {
        this.mojNick = mojNick;
        this.client = client;
        this.allUsers = allUsers;
        odbiorcaDoCzatWindowMap = new HashMap<>();

        lblUruchomionyUzytkownik.setText("Konto użytkownika: " + mojNick);

        CheckIfSthNewOnTheServerThread checkThread = new CheckIfSthNewOnTheServerThread(client, mojNick, odbiorcaDoCzatWindowMap);
        UserListListner userListListner = new UserListListner() {
            @Override
            public void onUserList(List<UserInfo> userList) {
                uzytkownicy.setModel(new UserListTableModel(userList));
            }
        };

        checkThread.addUserListListener(userListListner);

        new Thread(checkThread).start();

        wylogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (client != null) {
                    client.closeConnection();
                    LOGGER.info("Połączenie z serwerem zostało zakończone, użytkownik: " + "*" + mojNick + "*" + " wylogował się...");
                }
                closeWindow();
                if (czatWindow != null) {
                    czatWindow.closeWindow();
                }
                if (historiaWindow != null) {
                    historiaWindow.closeWindow();
                }
                MainWindow mainWindow = new MainWindow();
                mainWindow.showWindow();
            }
        });

        oAplikacjiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OAplikacjiDialog.openOAplikacjiDialog();
            }
        });

        historiaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historiaWindow = new HistoriaWindow();
                historiaWindow.showWindow();
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

    private void createUIComponents() {
        uzytkownicy = new JTable(new UserListTableModel(new ArrayList<>())) {
            public boolean isCellEditable(int data, int columnNames) {
                return false;
            } //zablokowanie edycji komorek tabeli
        };

        uzytkownicy.setRowSelectionAllowed(true);
        uzytkownicy.setFillsViewportHeight(true);
        uzytkownicy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        uzytkownicy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String kontaktZListyUzytkownikow = (String) uzytkownicy.getValueAt(uzytkownicy.getSelectedRow(), 0);
                    LOGGER.info("Wybrany kontakt z listy użytkowników: " + kontaktZListyUzytkownikow);
                    czatWindow = new CzatWindow(kontaktZListyUzytkownikow, client, mojNick);
                    czatWindow.showWindow();
                    odbiorcaDoCzatWindowMap.put(kontaktZListyUzytkownikow, czatWindow);

                    czatWindow.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                            odbiorcaDoCzatWindowMap.remove(kontaktZListyUzytkownikow);
                        }
                    });
                }
            }
        });
    }

    public void showKontaktyWindow() {
        setTitle("Komunikator - Kontakty");
        setContentPane(kontaktyWindow);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void closeWindow() {
        this.setVisible(false);
        this.dispose();
    }
}