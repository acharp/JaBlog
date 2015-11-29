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
    protected Object doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        String string_id = req.getRequestURI().split("/")[2];
        long id = Integer.parseInt(string_id);

        return MessagesRepository.getMessage(id);
    }


    @Override
    protected Object doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        JsonObject updt_info = getJsonParameters(req);
        if (!updt_info.has("text")) {
            throw new ApiException(400, "invalidRequest", "Invalid json body");
        }

        String string_id = req.getRequestURI().split("/")[2];
        long id = Integer.parseInt(string_id);

        Message old_msg = MessagesRepository.getMessage(id);

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

            return "Message with id " + id + " has been modified";

        } else {
            return "You didn't post this message, you can't modify it";
        }
    }


    @Override
    protected Object doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        String string_id = req.getRequestURI().split("/")[2];
        long id = Integer.parseInt(string_id);
        MessagesRepository.deleteMessage(id);
        return "Message with id " + id + " has been deleted";
    }

}
