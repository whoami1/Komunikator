package pl.wrzesien;

import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Micha³ Wrzesieñ on 2015-06-09.
 */
public class MessageDispatcher
{
    private UserService userService;
    private final ObjectOutputStream oos;
    private final Socket socket;
    private final Map<String, Communication> map;
    private UserName userName;

    MessageDispatcher(UserService userService, ObjectOutputStream oos, Socket socket, Map<String, Communication> map, UserName userName)
    {
        this.userService = userService;
        this.oos = oos;
        this.socket = socket;
        this.map = map;
        this.userName = userName;
    }

    public void dispatch (Object obj) throws Exception
    {
            String nazwa = "pl.wrzesien.handler." + obj.getClass().getSimpleName() + "Handler";
            Class cl = Class.forName(nazwa);

            Object o = cl.newInstance();
            Method method = cl.getMethod("execute",Object.class, UserService.class, ObjectOutputStream.class, Socket.class, Map.class, UserName.class);
            method.invoke(o, obj, userService, oos, socket, map, userName);
    }
}
