package pl.entities.response;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-16.
 */
public class TestowaWiadomoscResponse implements Serializable {
    private static final long serialVersionUID = 1;

    private boolean succes;
    private String username;
    private String text;

    public TestowaWiadomoscResponse(boolean succes, String username, String text) {

        this.succes = succes;
        this.username = username;
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public boolean isSucces() {
        return succes;
    }

    @Override
    public String toString() {
        return "pl.entities.response.TestowaWiadomoscResponse{" +
                "succes=" + succes +
                ", username='" + username + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
