package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-22.
 */
public class KontaktyWindow {
    private JButton historiaButton;
    private JButton dodajKontaktButton;
    private JTable Użytkownicy;
    private JPanel KontaktyWindow;
    private JButton oAplikacjiButton;
    private JLabel lblUruchomionyUzytkownik;
    private JButton testWiadDoSerwButton;
    private Zegar zegar1;

    private String nazwaUzytkownika;
    private Client client;

    public KontaktyWindow(Client client, String nazwaUzytkownika)
    {
        this.nazwaUzytkownika = nazwaUzytkownika;
        this.client = client;
        initComponents();
    }

    private void initComponents()
    {
        lblUruchomionyUzytkownik.setText("Konto użytkownika: " + nazwaUzytkownika);
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

        testWiadDoSerwButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String text = "Testujemy czy dziala";
                client.wyslanieTestowejWiadomosciNaSerwer(nazwaUzytkownika,text);

                if (client.wyslanieTestowejWiadomosciNaSerwer(nazwaUzytkownika,text))
                {
                    System.out.println(client.odebranieTestowejWiadomosciNaSerwer(nazwaUzytkownika,text));
                    JOptionPane.showMessageDialog(KontaktyWindow, "Udalo sie przeslac wiadomosc", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(KontaktyWindow, "Nie udalo sie przeslac wiadomosc", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        String[] columnNames = {"Nick", "Status"};
        Object[][] data = {{"Admin", "niedostępny"}};
        Użytkownicy = new JTable(data, columnNames);
        Użytkownicy.setFillsViewportHeight(true);

    }

    public void showKontaktyWindow() {
        JFrame frame = new JFrame("Komunikator - Kontakty");
        JPanel kontaktyWindow = new KontaktyWindow(client,nazwaUzytkownika).KontaktyWindow;
        frame.setContentPane(kontaktyWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}