package pl.wrzesien;

import pl.entities.response.MessageResponse;

import java.net.Socket;
import java.util.List;

/**
 * Created by Micha³ Wrzesieñ on 2015-04-21.
 */
public class Communication {
    private Socket socket;
    private List<MessageResponse> listOfMessageResponse;


    public Communication(Socket socket, List<MessageResponse> listOfMessageResponse) {
        this.socket = socket;
        this.listOfMessageResponse = listOfMessageResponse;
    }

    public Socket getSocket() {
        return socket;
    }

    public List<MessageResponse> getListOfMessageResponse() {
        return listOfMessageResponse;
    }
}
