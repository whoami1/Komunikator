package pl.entities.request;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-13.
 */
public class FileRequest implements Serializable {
    private byte[] plik;

    public FileRequest(byte[] plik) {
        this.plik = plik;
    }


    public byte[] getPlik() {
        return plik;
    }

    public void setPlik(byte[] plik) {
        this.plik = plik;
    }
}
