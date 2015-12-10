package fr.ecp.sio.jablog.data;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import fr.ecp.sio.jablog.model.User;

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

    public static UsersList getUsers() {
        return new UsersList(
                ObjectifyService.ofy()
                        .load()
                        .type(User.class)
                        .list(),
                "dummyCursor"
        );
    }

    public static long allocateNewId() {
        return new ObjectifyFactory().allocateId(User.class).getId();
    }

    public static void saveUser(User user) {
        user.id = ObjectifyService.ofy()
                .save()
                .entity(user)
                .now()
                .getId();
    }

    public static Void deleteUser(long id){
        return ObjectifyService.ofy()
                .delete()
                .type(User.class)
                .id(id)
                .now();
    }


    public static UsersList getUserFollowed(long id, int limit){
        return getUsers();
        //:TODO
    }

    public static UsersList getUserFollowed(String cursor, int limit){
        return getUsers();
        //:TODO
    }

    public static UsersList getUserFollowers(long id){
        return getUsers();
        //:TODO
    }

    public static UsersList getUserFollowers(String cursor, long id){
        return getUsers();
        //:TODO
    }

    public static class UsersList {
        public List<User> users;
        public String cursor;

        private UsersList(List<User> users, String cursor) {
            this.users = users;
            this.cursor = cursor;
        }
    }

    public static void setUserFollowed(long followerId, long followedId, boolean followed) {
        // To implement later
    }

}
