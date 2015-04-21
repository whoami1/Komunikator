package pl.wrzesien;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michał Wrzesień on 2015-03-08.
 */
public class MainWindow extends JFrame {
    private JComboBox cmbAdresSerwera;
    private JButton logowanieButton;
    private JButton rejestracjaButton;
    private JPanel MainWindow;

    public MainWindow() {
        initComponents();
    }

    private void initComponents() {

        //final MainWindow mainWindow = this;
        Client client = new Client();

        if (client.connect(getServerIpAddress())) {
            logowanieButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openLogowanieWindow(client);
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainWindow);
                    topFrame.dispose();
                }
            });

            rejestracjaButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    openRejestracjaWindow(client);
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(MainWindow);
                    topFrame.dispose();
                }
            });
        } else {
            JOptionPane.showMessageDialog(MainWindow, "Połączenie nie mogło zostać zreazlizowane...", "Błąd połączenia", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getServerIpAddress() {
        String ipAddress = cmbAdresSerwera.getSelectedItem().toString();
        System.out.println("Obecny adres IP: " + ipAddress);
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Komunikator");
        pl.wrzesien.MainWindow mainwindow = new MainWindow();
        frame.setContentPane(mainwindow.MainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

