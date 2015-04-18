package pl.wrzesien;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michał Wrzesień on 2015-03-28.
 */
public class UserService {
    private Session session;
    private ArrayList<String> userList = new ArrayList<>();

    public UserService() {
        this.session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close() {
        HibernateUtil.getSessionFactory().close();
    } //prawdopodobnie można zmienić na HibernateUtil.shutdown();

    public void newUser(String userNick, String userPassword) {
        //this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        User user = new User(userNick, userPassword);
        session.save(user);

        session.getTransaction().commit();
    }

    private User getUser(Integer userId) {
        session.beginTransaction();

        User result = (User) session.get(User.class, userId);

        session.getTransaction().commit();
        return result;
    }

    private void deleteUser(Integer userId) {
        session.beginTransaction();

        //jeśli będą jakieś relacje, na początek je usunąć
        User user = new User(userId);
        session.delete(user);

        session.getTransaction().commit();
    }

    public List<User> showUsers() {
        session.beginTransaction();

        List<User> result = session.createQuery("FROM User").list();

        session.getTransaction().commit();
        return result;
    }

    //checkCredentials dziala przy logowaniu
    public Boolean checkCredentials(String login, String password)
    {
        session.beginTransaction();

        List<Query> result = session.createQuery("FROM User WHERE userNick='" + login + "' AND userPassword='" + password + "'").list();
        session.getTransaction().commit();
        if(result.size() == 1)
            return true;
        else
            return false;
    }

    //chceckIfLoginExists dziala przy rejestracji
    public Boolean checkIfLoginExists(String login)
    {
        session.beginTransaction();

        List<Query> result = session.createQuery("FROM User WHERE userNick='" + login + "'").list();
        session.getTransaction().commit();
        if(result.size() == 1) //jeśli znajdzie to true, to znaczy, że taki użytkownik już istnieje
            return true;
        else
            return false;
    }
}
