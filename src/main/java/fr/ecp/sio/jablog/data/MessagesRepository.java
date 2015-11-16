package fr.ecp.sio.jablog.data;

import com.googlecode.objectify.ObjectifyService;
import fr.ecp.sio.jablog.model.Message;

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
}
