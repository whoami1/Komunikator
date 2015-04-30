package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-24.
 */
public class CzatWindow {
    private JPanel czatWindow;
    private JTextArea txtCzat;
    private JTextField txtWiadomosc;
    private JButton wyslijButton;
    private JLabel lblUruchomionyUzytkownik;
    private UserInfo userInfo;
    private Client client;
    private String nazwaUzytkownika;

    public CzatWindow(UserInfo userInfo, Client client, String nazwaUzytkownika) {
        this.userInfo = userInfo;
        this.client = client;
        this.nazwaUzytkownika = nazwaUzytkownika;

        txtCzat.setEditable(false);
        lblUruchomionyUzytkownik.setText("Użytkownik: " + "\"" + nazwaUzytkownika + "\"" + " rozmowa z: " + "\"" + userInfo.getUserNick() + "\"");
        
        wyslijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //txtCzat.append(userInfo.getUserNick()+ ": " + txtWiadomosc.getText() + "\n");
                txtCzat.append(nazwaUzytkownika + ": " + txtWiadomosc.getText() + "\n");
                client.wyslanieWiadomosciNaSerwer(userInfo.getUserNick(), txtWiadomosc.getText());
                txtWiadomosc.setText(null);
            }
        });


    }

    public void showWindow() {
        JFrame frame = new JFrame("Komunikator - Czat");
        frame.setContentPane(czatWindow);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        txtWiadomosc.requestFocusInWindow();
        czatWindow.getRootPane().setDefaultButton(wyslijButton);
    }
}
