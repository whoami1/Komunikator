package pl.wrzesien;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JButton odbierzButton;

    private String nazwaUzytkownika;
    private Client client;

    public KontaktyWindow(Client client, String nazwaUzytkownika) {
        this.nazwaUzytkownika = nazwaUzytkownika;
        this.client = client;
        initComponents();
    }

    private void initComponents() {
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
            public void actionPerformed(ActionEvent e) {

                String text = "Testujemy czy dziala";
                client.wyslanieTestowejWiadomosciNaSerwer("admin", text);

            }
        });

        odbierzButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.odebranieTestowejWiadomosciNaSerwer();
            }
        });

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        String[] columnNames = {"Nick", "Status"};
        Object[][] data = {
                {"admin", "niedostępny"},
                {"user", "niedostępny"}
        };
        Użytkownicy = new JTable(data, columnNames);
        Użytkownicy.setRowSelectionAllowed(true);
       // Użytkownicy.setFocusable(false);
        Użytkownicy.setFillsViewportHeight(true);
        Użytkownicy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Użytkownicy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 1)
                {
                    String user = (String) Użytkownicy.getValueAt(Użytkownicy.getSelectedRow(), 0);
                    System.out.println("***************" + user);
                    CzatWindow czatWindow = new CzatWindow(new User(user), client);
                    czatWindow.showWindow();
                }
            }
        });

    }

    public void showKontaktyWindow() {
        JFrame frame = new JFrame("Komunikator - Kontakty");
        JPanel kontaktyWindow = new KontaktyWindow(client, nazwaUzytkownika).KontaktyWindow;
        frame.setContentPane(kontaktyWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}