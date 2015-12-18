package fr.ecp.sio.jablog.api;

import com.googlecode.objectify.Ref;
import fr.ecp.sio.jablog.data.MessagesRepository;
import fr.ecp.sio.jablog.model.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by charpi on 30/10/15.
 */
public class MessagesServlet extends JsonServlet {

    @Override
    protected List<Message> doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: filter the messages that the user can see (security!)
        // TODO: filter the list based on some parameters (order, limit, scope...)
        // TODO: e.g. add a parameter to get the messages of a user given its id (i.e. /messages?userr=256439)
        return MessagesRepository.getMessages();
    }

    @Override
    protected Message doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        Message message = getJsonRequestBody(req, Message.class);
        if (message == null) {
            throw new ApiException(400, "invalidRequest", "Invalid JSON body");
        }

        if (!(message.text.length() > 0)){
            throw new ApiException(400, "invalidMessageContent", "Text message is empty");
        }

        message.date = new Date();
        message.user = Ref.create(getAuthenticatedUser(req));
        message.id = null;

        MessagesRepository.insertMessage(message);

        return message;
    }
}
