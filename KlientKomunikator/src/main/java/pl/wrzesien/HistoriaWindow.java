package pl.wrzesien;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-09.
 */
public class HistoriaWindow extends JFrame
{

    private static final Logger LOGGER = LoggerFactory.getLogger(pl.wrzesien.HistoriaWindow.class);

    private JPanel historiaWindow;
    private JTable tblArchiwum;
    private JTextArea txtTekstWiadomosciZArchiwum;

    public HistoriaWindow()
    {
        txtTekstWiadomosciZArchiwum.setEditable(false);
    }

    private void createUIComponents()
    {
        String path = "archiwum";
        String columnNames[] = new String[]{"odbiorca_data"};

        File f = new File(path);
        Object[][] data = new Object[0][0];

        if (f.isDirectory())
        {
            ArrayList<String> names = new ArrayList<>(Arrays.asList(f.list()));

            data = new Object[names.size()][2];
            for (int i = 0; i < names.size(); i++)
            {
                if (names.get(i).contains("."))
                {
                    data[i][0] = names.get(i).substring(0, names.get(i).lastIndexOf('.'));
                }
            }
        } else
        {
            f.mkdir();
        }

        tblArchiwum = new JTable(data, columnNames)
        {
            public boolean isCellEditable(int data, int columnNames)
            {
                return false;
            } //zablokowanie edycji komorek tabeli
        };

        tblArchiwum.setRowSelectionAllowed(true);
        tblArchiwum.setFillsViewportHeight(true);
        tblArchiwum.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblArchiwum.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    txtTekstWiadomosciZArchiwum.setText("");
                    String rozszerzenie = ".txt";
                    String nazwaPliku = (String) tblArchiwum.getValueAt(tblArchiwum.getSelectedRow(), 0);
                    LOGGER.info("Uruchomiono plik: " + nazwaPliku);

                    HistoriaZapisOdczyt historiaZapisOdczyt = new HistoriaZapisOdczyt();
                    try
                    {
                        ArrayList<String> tekstRozmowyTablica = historiaZapisOdczyt.odczytPliku(path + "\\" + nazwaPliku + rozszerzenie);
                        for (String tekstRozmowy : tekstRozmowyTablica)
                        {
                            txtTekstWiadomosciZArchiwum.append(tekstRozmowy);
                        }
                    } catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    public void showWindow()
    {
        setTitle("Komunikator - Historia");
        setContentPane(historiaWindow);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void closeWindow()
    {
        this.setVisible(false);
        this.dispose();
    }
}
