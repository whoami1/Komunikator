package pl.entities.request;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-13.
 */
public class FileRequest implements Serializable {
    private byte[] plik;
    private String username;
    private String filename;

    public FileRequest(String username, byte[] plik, String filename) {
        this.plik = plik;
        this.username = username;
        this.filename = filename;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
