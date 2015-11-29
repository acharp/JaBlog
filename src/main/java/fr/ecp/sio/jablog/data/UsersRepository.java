package fr.ecp.sio.jablog.data;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;
import fr.ecp.sio.jablog.model.Message;
import fr.ecp.sio.jablog.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;


/**
 * Created by charpi on 02/11/15.
 */
public class UsersRepository {

    static {
        ObjectifyService.register(User.class);
    }


    public static User getUserByLogin(String login) {
        return ObjectifyService.ofy()
                .load()
                .type(User.class)
                .filter("login", login)
                .first()
                .now();
    }

    public static User getUserByEmail(String email) {
        return ObjectifyService.ofy()
                .load()
                .type(User.class)
                .filter("email", email)
                .first()
                .now();
    }

    public static User getUser(long id) {
        return ObjectifyService.ofy()
                .load()
                .type(User.class)
                .id(id)
                .now();
    }

    public static List<User> getUsers(){
        return ObjectifyService.ofy()
                .load()
                .type(User.class)
                .list();
    }

    public static long insertUser(User user) {
        return ObjectifyService.ofy()
                .save()
                .entity(user)
                .now()
                .getId();
    }

    public static Void deleteUser(long id){
        return ObjectifyService.ofy()
                .delete()
                .entity(getUser(id))
                .now();
    }

}
