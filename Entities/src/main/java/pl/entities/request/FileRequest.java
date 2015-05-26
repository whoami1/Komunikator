package pl.entities.request;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-13.
 */
public class FileRequest implements Serializable {
    private byte[] plik;
    private String username;

    public FileRequest(String username, byte[] plik) {
        this.plik = plik;
        this.username = username;
    }


    public byte[] getFile() {
        return plik;
    }

    public void setPlik(byte[] plik) {
        this.plik = plik;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
