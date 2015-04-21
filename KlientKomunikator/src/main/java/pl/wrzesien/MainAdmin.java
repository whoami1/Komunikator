package pl.wrzesien;

/**
 * Created by p.wegrzynski on 21/04/2015.
 */
public class MainAdmin {
    public static void main(String[] args) {
        Client client = new Client();
        client.connect("192.168.0.103");
        client.login("admin", "pass");
        KontaktyWindow admin = new KontaktyWindow(client, "admin");
        admin.showKontaktyWindow();
    }
}
