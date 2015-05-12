package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Michał Wrzesień on 2015-03-15.
 */
public class RejestracjaWindow extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(RejestracjaWindow.class);

    private JTextField txtLogin;
    private JPasswordField txtHaslo;
    private JButton zarejestrujButton;
    private JButton anulujButton;
    private JPanel rejestracjaWindow;

    private MainWindow mainWindow;
    private Client client;

    public RejestracjaWindow(MainWindow mainWindow, Client client) {
        this.mainWindow = mainWindow;
        this.client = client;

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeRejestracjaWindow();
                mainWindow.showWindow();
            }
        });

        zarejestrujButton.addActionListener(new ActionListener() {
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
            JOptionPane.showMessageDialog(rejestracjaWindow, "Nie wpisano jeszcze loginu...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else if (getHaslo().isEmpty()) {
            JOptionPane.showMessageDialog(rejestracjaWindow, "Nie wpisano jeszcze hasła...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else {
            registerIn();
        }
    }

    private void registerIn() {
        if (client.register(getLogin(), getHaslo())) {
            JOptionPane.showMessageDialog(rejestracjaWindow, "Podany użytkownik już istnieje...", "Błąd rejestracji", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(rejestracjaWindow, "Rejestracja zakończyła się sukcesem...", "Rejestracja", JOptionPane.INFORMATION_MESSAGE);
            LOGGER.info("Zarejestrowano nastepujacego uzytkownika: " + getLogin());
        }
    }

    public String getLogin() {
        return String.valueOf(txtLogin.getText());
    }

    public String getHaslo() {
        return String.valueOf(txtHaslo.getPassword());
    }

    public void showRejestracjaWindow() {
        setContentPane(rejestracjaWindow);
        setTitle("Komunikator - Rejestracja");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        getRootPane().setDefaultButton(zarejestrujButton);
    }

    public void closeRejestracjaWindow()
    {
        this.setVisible(false);
        this.dispose();
    }

}
