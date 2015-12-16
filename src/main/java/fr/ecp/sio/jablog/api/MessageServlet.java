package fr.ecp.sio.jablog.api;

import com.google.gson.JsonObject;
import com.googlecode.objectify.Ref;
import fr.ecp.sio.jablog.data.MessagesRepository;
import fr.ecp.sio.jablog.model.Message;
import fr.ecp.sio.jablog.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

/**
 * Created by charpi on 30/10/15.
 */
public class MessageServlet extends JsonServlet {

    @Override
    protected Message doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {

        String string_id = req.getRequestURI().split("/")[2];
        long id = Long.parseLong(string_id);

        return getMessage(id);
    }


    @Override
    protected Message doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        JsonObject updt_info = getJsonRequestBody(req);
        if (!updt_info.has("text")) {
            throw new ApiException(400, "invalidRequest", "Invalid json body");
        }

        String string_id = req.getRequestURI().split("/")[2];
        long id = Long.parseLong(string_id);

        Message old_msg = getMessage(id);

        // Check that the user who wants to modify the message is the owner of the message
        if (auth_user.equals(old_msg.user.getValue())) {

            String text = updt_info.get("text").getAsString();
            if (!(text.length() > 0)){
                throw new ApiException(400, "invalidMessageContent", "Text message is empty");
            }

            Message new_msg = new Message();
            new_msg.id = old_msg.id;
            new_msg.text = text;
            new_msg.date = new Date();
            new_msg.user = Ref.create(auth_user);

            MessagesRepository.deleteMessage(old_msg.id);
            MessagesRepository.insertMessage(new_msg);

            return new_msg;

        } else {
            throw new ApiException(403, "actionForbidden", "Message " + string_id + " isn't yours");
        }
    }


    @Override
    protected Void doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        String string_id = req.getRequestURI().split("/")[2];
        long id = Long.parseLong(string_id);

        Message message = getMessage(id);

        if (auth_user.equals(message.user.getValue())) {
            MessagesRepository.deleteMessage(id);
        } else {
            throw new ApiException(403, "actionForbidden", "Message " + string_id + " isn't yours");
        }

        return null;
    }


    // Return a message from an id and throw Exception if doesn't exist
    private Message getMessage(long id) throws ApiException {
        Message message = MessagesRepository.getMessage(id);
        if (message == null) {
            throw new ApiException(404, "Resource not found", "Message doesn't exist");
        } else return message;
    }

}
