package pl.entities.response;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-05-13.
 */
public class FileResponse implements Serializable {
    private boolean success;

    public FileResponse(boolean success) {
        this.success = success;
    }
}
