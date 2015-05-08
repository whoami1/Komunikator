package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-24.
 */
public class CzatWindow extends JFrame {
    private JPanel czatWindow;
    private JTextArea txtRozmowaWOknieCzatu;
    private JTextField txtWiadomosc;
    private JButton wyslijButton;
    private JLabel lblUruchomionyUzytkownik;
    private String odbiorca;
    private Client client;
    private String nadawca;

    public CzatWindow(String odbiorca, Client client, String nadawca) {
        this.client = client;
        this.odbiorca = odbiorca;
        this.nadawca = nadawca;

        txtRozmowaWOknieCzatu.setEditable(false);
        lblUruchomionyUzytkownik.setText("Użytkownik: " + "\"" + nadawca + "\"" + " rozmowa z: " + "\"" + odbiorca + "\"");

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
    }

    public void setTxtRozmowaWOknieCzatu(String nadawca, String txtRozmowaWOknieCzatu) {
        this.txtRozmowaWOknieCzatu.append(nadawca + ": " + txtRozmowaWOknieCzatu + "\n");
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

    public void closeCzatWindow() {
        this.setVisible(false);
        this.dispose();
    }
}
