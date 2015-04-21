package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-15.
 */
public class RejestracjaWindow extends JFrame {
    private JTextField txtLogin;
    private JPasswordField txtHaslo;
    private JButton zarejestrujButton;
    private JButton anulujButton;
    private JPanel rejestracjaWindow;

    private Client client;

    public RejestracjaWindow(Client client) {
        this.client = client;

        anulujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(rejestracjaWindow);
                topFrame.dispose();
                MainWindow.getFrames()[0].setVisible(true);
            }
        });

        zarejestrujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIfAllCredentialsEntered();
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
        }
    }

    public String getLogin() {
        String text = txtLogin.getText().toString();
        System.out.println("Login: " + text);
        return text;
    }

    public String getHaslo() {
        String text = String.valueOf(txtHaslo.getPassword());
        System.out.println("Haslo: " + text);
        return text;
    }

    public void showWindow() {
        setContentPane(rejestracjaWindow);
        setTitle("Komunikator - Rejestracja");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(zarejestrujButton);
    }
}
