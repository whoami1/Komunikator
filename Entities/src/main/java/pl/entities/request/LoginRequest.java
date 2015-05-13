package pl.entities.request;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-14.
 */
public class LoginRequest implements Serializable {
    private String login;
    private String password;

    public LoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "pl.entity.request.pl.entities.request.LoginRequest{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
