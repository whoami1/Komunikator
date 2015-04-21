package pl.entities.response;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-14.
 */
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 1;

    private boolean succes;

    public LoginResponse(boolean succes) {
        this.succes = succes;
    }

    public boolean isSucces() {
        return succes;
    }

    @Override
    public String toString() {
        return "pl.entities.response.LoginResponse{" +
                "succes=" + succes +
                '}';
    }
}
