package pl.wrzesien;

import javax.swing.*;
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
    private JTable uzytkownicy;
    private JPanel kontaktyWindow;
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
        uzytkownicy = new JTable(data, columnNames) {
            public boolean isCellEditable (int data, int columnNames)
            {
                return false;
            } //zablokowanie edycji tabeli
        };

        uzytkownicy.setRowSelectionAllowed(true);
       // uzytkownicy.setFocusable(false);
        uzytkownicy.setFillsViewportHeight(true);
        uzytkownicy.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        uzytkownicy.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String user = (String) uzytkownicy.getValueAt(uzytkownicy.getSelectedRow(), 0);
                    System.out.println("***************" + user);
                    CzatWindow czatWindow = new CzatWindow(new User(user), client, nazwaUzytkownika);
                    czatWindow.showWindow();
                }
            }
        });

    }

    public void showKontaktyWindow() {
        JFrame frame = new JFrame("Komunikator - Kontakty");
        JPanel kontaktyWindow = new KontaktyWindow(client, nazwaUzytkownika).kontaktyWindow;
        frame.setContentPane(kontaktyWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}