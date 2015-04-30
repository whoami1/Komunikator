package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainWindow);
                topFrame.dispose();
            }
        });

        rejestracjaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openRejestracjaWindow(client);
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainWindow);
                topFrame.dispose();
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
        LogowanieWindow logowanieWindow = new LogowanieWindow(client);
        logowanieWindow.showWindow();
    }

    public void openRejestracjaWindow(Client client) {
        RejestracjaWindow rejestracjaWindow = new RejestracjaWindow(client);
        rejestracjaWindow.showWindow();
    }

    public void showWindow()
    {
        JFrame frame = new JFrame("Komunikator");
        frame.setContentPane(mainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        mainWindow.getRootPane().setDefaultButton(polaczZSerweremButton);
    }

    public static void main(String[] args)
    {
        pl.wrzesien.MainWindow mainWindow = new MainWindow();
        mainWindow.showWindow();
    }
}

