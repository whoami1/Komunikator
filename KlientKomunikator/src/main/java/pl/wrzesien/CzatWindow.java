package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-24.
 */
public class CzatWindow {
    private JPanel CzatWindow;
    private JTextArea txtCzat;
    private JTextField txtWiadomosc;
    private JButton wyslijButton;
    private User user;
    private Client client;


    public CzatWindow(User user, Client client) {
        this.user = user;
        this.client = client;
        wyslijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtCzat.setText(user.getUserNick()+ ": " + txtWiadomosc.getText());
                client.wyslanieTestowejWiadomosciNaSerwer(user.getUserNick(), txtWiadomosc.getText());
            }
        });
    }

    public void showWindow() {
        JFrame frame = new JFrame("Komunikator - Czat");
        frame.setContentPane(CzatWindow);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
