package fr.ecp.sio.jablog.api;

import com.google.gson.JsonObject;
import com.googlecode.objectify.Ref;
import fr.ecp.sio.jablog.data.MessagesRepository;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.Message;
import fr.ecp.sio.jablog.model.User;
import fr.ecp.sio.jablog.utils.ValidationUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by charpi on 30/10/15.
 */
public class UserServlet extends JsonServlet{

    @Override
    protected User doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {

        // Retrieve id of the needed user in the url
        String string_id = req.getRequestURI().split("/")[2];
        long id = Long.parseLong(string_id);

        User user = getUser(id);

        // If authenticated user is the same as the user returned we return all infos
        if (getAuthenticatedUser(req).id == id) {
            return user;
        } else {
            User res_user = new User();
            res_user.login = user.login;
            res_user.id = user.id;
            res_user.avatar = user.avatar;
            return res_user;
        }

    }


    @Override
    protected User doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        JsonObject updt_info = getJsonRequestBody(req);

        String string_id = req.getRequestURI().split("/")[2];
        long id = Long.parseLong(string_id);
        if (auth_user.id != id){
            throw new ApiException(403, "actionForbidden", "This isn't your user account");
        }
        User old_user = getUser(id);

        User new_user = new User();
        new_user.id = old_user.id;

        if (updt_info.has("login")) {
            String login = updt_info.get("login").getAsString();
            if (!ValidationUtils.validateLogin(login)) {
                throw new ApiException(400, "invalidLogin", "Login did not match the specs");
            }
            if (UsersRepository.getUserByLogin(login) != null) {
                throw new ApiException(400, "duplicateLogin", "Duplicate login");
            }
            new_user.login = login;
        } else { new_user.login = old_user.login; }

        if (updt_info.has("email")) {
            String email = updt_info.get("email").getAsString();
            if (!ValidationUtils.validateEmail(email)) {
                throw new ApiException(400, "invalidEmail", "Email did not match the specs");
            }
            if (UsersRepository.getUserByEmail(email) != null) {
                throw new ApiException(400, "duplicateEmail", "Duplicate email");
            }
            new_user.email = email;
        } else { new_user.email = old_user.email; }

        if (updt_info.has("password")) {
            String password = updt_info.get("password").getAsString();
            if (!ValidationUtils.validateLogin(password)) {
                throw new ApiException(400, "invalidPassword", "Password did not match the specs");
            }
            new_user.password = DigestUtils.sha256Hex(password + new_user.id);
        } else { new_user.password = old_user.password; }

        if (updt_info.has("avatar")) {
           new_user.avatar = updt_info.get("avatar").getAsString();
        } else { new_user.avatar = old_user.avatar; }

        // TODO: Handle special parameters like "followed=true" to create or destroy following relationships

        UsersRepository.deleteUser(old_user.id);
        UsersRepository.saveUser(new_user);

        return new_user;
    }


    @Override
    protected Void doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        String string_id = req.getRequestURI().split("/")[2];
        long id = Long.parseLong(string_id);
        if (auth_user.id != id){
            throw new ApiException(403, "actionForbidden", "This isn't your user account");
        }

        // Delete all the messages of the user
        List<Message> test = MessagesRepository.getMessages();
        for (Message message : test){
            if ( message.user.get() == auth_user) {
                MessagesRepository.deleteMessage(message.id);
            }
        }

        // TODO: Delete the relationships of the user

        UsersRepository.deleteUser(id);

        return null;
    }


    // Return a user from an id and throw Exception if doesn't exist
    private User getUser(long id) throws ApiException {
        User user = UsersRepository.getUser(id);
        if (user == null) {
            throw new ApiException(404, "Resource not found", "User doesn't exist");
        } else return user;
    }
}
