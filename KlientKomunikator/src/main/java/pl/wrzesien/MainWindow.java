package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Created by Michał Wrzesień on 2015-03-08.
 */
public class MainWindow extends JFrame {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainWindow.class);

    private JComboBox cmbAdresSerwera;
    private JButton logowanieButton;
    private JButton rejestracjaButton;
    private JPanel mainWindow;
    private JButton polaczZSerweremButton;

    private Client client = null;

    public MainWindow() {
        //Client client = new Client();
        logowanieButton.setEnabled(false);
        rejestracjaButton.setEnabled(false);

        polaczZSerweremButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client = new Client();
                if (client.connect(getServerIpAddress())) {
                    JOptionPane.showMessageDialog(mainWindow, "Połączenie z serwerem zostało ustanowione...", "Informacja o połączeniu", JOptionPane.INFORMATION_MESSAGE);
                    LOGGER.info("Połączenie z serwerem zostało ustanowione...");
                    polaczZSerweremButton.setEnabled(false);
                    logowanieButton.setEnabled(true);
                    rejestracjaButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Połączenie nie mogło zostać zrealizowane... Spróbuj ponownie...", "Błąd połączenia", JOptionPane.ERROR_MESSAGE);
                    LOGGER.info("Połączenie nie mogło zostać zrealizowane... Spróbuj ponownie...");
                }
            }
        });

        logowanieButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openLogowanieWindow(client);
                closeMainWindow();
            }
        });

        rejestracjaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRejestracjaWindow(client);
                closeMainWindow();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    if (client != null)
                    {
                        client.closeConnection();
                        LOGGER.info("Połączenie z serwerem zostało zakończone...");
                    }
                } catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }

    public String getServerIpAddress() {
        String ipAddress = cmbAdresSerwera.getSelectedItem().toString();
        LOGGER.info("Obecny adres IP: " + ipAddress);
        return ipAddress;
    }

    public void setCmbAdresSerwera(JComboBox cmbAdresSerwera) {
        this.cmbAdresSerwera = cmbAdresSerwera;
    }

    public void openLogowanieWindow(Client client) {
        LogowanieWindow logowanieWindow = new LogowanieWindow(this, client);
        logowanieWindow.showLogowanieWindow();
    }

    public void openRejestracjaWindow(Client client) {
        RejestracjaWindow rejestracjaWindow = new RejestracjaWindow(this, client);
        rejestracjaWindow.showRejestracjaWindow();
    }

    public void showMainWindow() {
        setTitle("Komunikator");
        setContentPane(mainWindow);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        mainWindow.getRootPane().setDefaultButton(polaczZSerweremButton);
    }

    public void closeMainWindow() {
        this.setVisible(false);
        this.dispose();
    }

    public static void main(String[] args) {
        pl.wrzesien.MainWindow mainWindow = new MainWindow();
        mainWindow.showMainWindow();
    }
}

