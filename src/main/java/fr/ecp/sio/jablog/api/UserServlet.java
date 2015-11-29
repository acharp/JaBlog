package fr.ecp.sio.jablog.api;

import com.google.gson.JsonObject;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.User;
import fr.ecp.sio.jablog.utils.ValidationUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by charpi on 30/10/15.
 */
public class UserServlet extends JsonServlet{

    @Override
    protected Object doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        // Retrieve id of the needed user in the url
        String string_id = req.getRequestURI().split("/")[2];
        long id = Integer.parseInt(string_id);

        return UsersRepository.getUser(id);

    }


    @Override
    protected Object doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        JsonObject updt_info = getJsonParameters(req);

        String string_id = req.getRequestURI().split("/")[2];
        long id = Integer.parseInt(string_id);
        User old_user = UsersRepository.getUser(id);

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

        UsersRepository.deleteUser(old_user.id);
        UsersRepository.insertUser(new_user);

        return "User with id " + id + " has been modified";
    }


    @Override
    protected Object doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        String string_id = req.getRequestURI().split("/")[2];
        long id = Integer.parseInt(string_id);
        UsersRepository.deleteUser(id);
        return "User with id " + id + " has been deleted";
    }
}
