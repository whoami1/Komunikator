package pl.entities.response;

import java.io.Serializable;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-14.
 */
public class RegistrationResponse implements Serializable
{
    private boolean succes;

    public RegistrationResponse(boolean succes)
    {
        this.succes = succes;
    }

    public boolean isSucces()
    {
        return succes;
    }

    @Override
    public String toString()
    {
        return "pl.entities.response.RegistrationResponse{" +
                "succes=" + succes +
                '}';
    }
}
