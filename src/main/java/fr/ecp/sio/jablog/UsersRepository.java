package fr.ecp.sio.jablog;

import fr.ecp.sio.jablog.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
    }

    public static User getUser(String login) {


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
            else {
                return null;
            }
        }
    }
}
