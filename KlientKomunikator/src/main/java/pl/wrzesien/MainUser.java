package pl.wrzesien;

import java.util.ArrayList;

/**
 * Created by p.wegrzynski on 21/04/2015.
 */
public class MainUser {
    public static void main(String[] args) {
        Client client = new Client();
        client.connect("127.0.0.1");
        client.login("user", "pass");
        KontaktyWindow admin = new KontaktyWindow(client, "user", new ArrayList<>());
        admin.showKontaktyWindow();
    }
}
