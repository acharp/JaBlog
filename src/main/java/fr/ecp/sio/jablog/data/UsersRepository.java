package fr.ecp.sio.jablog.data;

import fr.ecp.sio.jablog.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.googlecode.objectify.ObjectifyService.ofy;


/**
 * Created by charpi on 02/11/15.
 */
public class UsersRepository {

    private static final List<User> mUsers = new ArrayList<>();

    static {
        User john = new User();
        john.id = 2;
        john.login = "john";
        john.password = DigestUtils.sha256Hex("toto" + john.id);
        mUsers.add(john);

        User igor = new User();
        igor.id = 3;
        igor.login = "igor";
        igor.password = DigestUtils.sha256Hex("passigor" + igor.id);
        ofy().save().entity(igor).now();
    }


    public static User getUser(String login) {

        List<User> users = ofy().load().type(User.class).list();
        // ==>> Remplacer mUsers par users dans les boucles suivantes et c'est ok

        // Simple iteration
        for (User user : mUsers) {
            if (user.login.equals(login)) return user;
        }
        return null;

        // OU
        // Lambda + streams (disponible Java 8+)
        /*
        return mUsers.stream()
                // On peut appliquer un filtre avec un prédicat en créant une instance anonyme de prédicat
                /*
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) {
                        return user.login.equals(login);
                    }
                })
                */
        /*
                // Mais la syntaxe suivante avec l'expression lambda évite de créer une instance anonyme
                .filter(user -> user.login.equals(login))
                .findFirst() // stream() permet d'enchainer les fonctions sur notre liste. Peut s'appliquer à tout iterable ou collection
                // Le streaming est particulièrement utile quand l'iterable/la collection n'est pas encore en mèmoire au moment
                // de la compilation. Très puissant notamment pour les curseurs de base de données.
                .get();
        */
    }

    public static User getUser(long id) {
        for (User user : mUsers) {
            if (user.id == id) return user;
        }
        return null;
    }

}
