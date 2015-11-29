package fr.ecp.sio.jablog.data;

import com.googlecode.objectify.ObjectifyService;
import fr.ecp.sio.jablog.model.Message;

import java.util.List;

/**
 * Created by charpi on 01/11/15.
 */
public class MessagesRepository {

    static {
        ObjectifyService.register(Message.class);
    }

    public static Message getMessage(long id) {
        return ObjectifyService.ofy()
                .load()
                .type(Message.class)
                .id(id)
                .now();
    };

    public static List<Message> getMessages() {
        return ObjectifyService.ofy()
                .load()
                .type(Message.class)
                .list();
    }

    public static long insertMessage(Message message) {
        return ObjectifyService.ofy()
                .save()
                .entity(message)
                .now()
                .getId();
    }

    public static Void deleteMessage(long id){
        return ObjectifyService.ofy()
                .delete()
                .entity(getMessage(id))
                .now();
    }
}
