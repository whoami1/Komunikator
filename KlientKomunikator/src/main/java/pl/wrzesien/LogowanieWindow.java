package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-10.
 */
public class LogowanieWindow extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.LogowanieWindow.class);
    private JPasswordField txtHaslo;
    private JTextField txtLogin;
    private JPanel LogowanieWindow;
    private JButton anulujButton;
    private JButton zalogujButton;
    private Client client;

    public LogowanieWindow(Client client) {
        this.client = client;

        //final pl.wrzesien.LogowanieWindow logowanieWindow = this;
        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(LogowanieWindow);
                topFrame.dispose();
                MainWindow.getFrames()[0].setVisible(true);
            }
        });

        zalogujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAllCredentialsEntered();
            }
        });
    }

    private void checkIfAllCredentialsEntered() {
        if (getLogin().isEmpty()) {
            JOptionPane.showMessageDialog(LogowanieWindow, "Nie wpisano jeszcze loginu...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else if (getHaslo().isEmpty()) {
            JOptionPane.showMessageDialog(LogowanieWindow, "Nie wpisano jeszcze hasła...", "Informacja", JOptionPane.INFORMATION_MESSAGE);
        } else {
            logIn();
        }
    }

    private void logIn() {
        if (client.login(getLogin(), getHaslo())) {
            List<User> users = client.readUserSet();
            LOGGER.info(users.toString());
            KontaktyWindow kontaktyWindow = new KontaktyWindow(client, getLogin());
            kontaktyWindow.showKontaktyWindow();
            ((JFrame) SwingUtilities.getWindowAncestor(LogowanieWindow)).setVisible(false);
        } else {
            JOptionPane.showMessageDialog(LogowanieWindow, "Nieprawidłowy login albo hasło...", "Błąd uwierzytelniania", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String getLogin() {
        String login = txtLogin.getText().toString();
        System.out.println("Login: " + login);
        return login;
    }

    public String getHaslo() {
        String haslo = String.valueOf(txtHaslo.getPassword());
        System.out.println("Haslo: " + haslo);
        return haslo;
    }

    public void showWindow() {
        setContentPane(LogowanieWindow);
        setTitle("Komunikator - Logowanie");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(zalogujButton);
    }
}
