package fr.ecp.sio.jablog.api;

import com.googlecode.objectify.Ref;
import fr.ecp.sio.jablog.data.MessagesRepository;
import fr.ecp.sio.jablog.model.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by charpi on 30/10/15.
 */
public class MessagesServlet extends JsonServlet {

    @Override
    protected Object doGet(HttpServletRequest req) throws ServletException, IOException {
        return MessagesRepository.getMessages();
    }

    @Override
    protected Object doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        Message message = getJsonParameters(req, Message.class);
        if (message == null) {
            throw new ApiException(400, "invalidRequest", "Invalid JSON body");
        }

        // TODO: validate message

        message.user = Ref.create(getAuthenticatedUser(req));
        message.id = null;
        message.id = MessagesRepository.insertMessage(message);

        return message;
    }
}
