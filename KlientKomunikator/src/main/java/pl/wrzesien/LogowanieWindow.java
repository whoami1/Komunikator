package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-10.
 */
public class LogowanieWindow extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.LogowanieWindow.class);
    private JPasswordField txtHaslo;
    private JTextField txtLogin;
    private JPanel logowanieWindow;
    private JButton anulujButton;
    private JButton zalogujButton;

    private MainWindow mainWindow;
    private Client client;

    public LogowanieWindow(MainWindow mainWindow, Client client) {
        this.mainWindow = mainWindow;
        this.client = client;

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeLogowanieWindow();
                //MainWindow mainWindow = new MainWindow();
                mainWindow.showMainWindow();
            }
        });

        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAllCredentialsEntered();
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

    private void checkIfAllCredentialsEntered() {
        if (getLogin().isEmpty()) {
            JOptionPane.showMessageDialog(logowanieWindow, "Nie wpisano jeszcze loginu...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else if (getHaslo().isEmpty()) {
            JOptionPane.showMessageDialog(logowanieWindow, "Nie wpisano jeszcze hasła...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else {
            logIn();
        }
    }

    private void logIn() {
        if (client.login(getLogin(), getHaslo())) {
            List<UserInfo> allUsers = client.listaWszystkichUzytkownikow();
            LOGGER.info("Zalogowano uzytkownika: " + getLogin());
            LOGGER.info("Zarejestrowani uzytkownicy: " + allUsers.toString());

            KontaktyWindow kontaktyWindow = new KontaktyWindow(client, getLogin(), allUsers);
            kontaktyWindow.showKontaktyWindow();
            closeLogowanieWindow();
        } else {
            JOptionPane.showMessageDialog(logowanieWindow, "Nieprawidłowy login albo hasło...", "Błąd uwierzytelniania", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String getLogin() {
        return String.valueOf(txtLogin.getText());
    }

    public String getHaslo() {
        return String.valueOf(txtHaslo.getPassword());
    }

    public void showLogowanieWindow() {
        setTitle("Komunikator - Logowanie");
        setContentPane(logowanieWindow);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); //okno na srodku ekranu
        setVisible(true);
        getRootPane().setDefaultButton(zalogujButton);
    }

    public void closeLogowanieWindow()
    {
        this.setVisible(false);
        this.dispose();
    }

}
