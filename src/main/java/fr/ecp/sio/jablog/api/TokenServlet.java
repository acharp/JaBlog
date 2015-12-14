package fr.ecp.sio.jablog.api;


import com.google.gson.JsonObject;
import fr.ecp.sio.jablog.utils.TokenUtils;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.utils.ValidationUtils;
import fr.ecp.sio.jablog.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by charpi on 02/11/15.
 */
public class TokenServlet extends JsonServlet{

    @Override
    protected String doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        JsonObject params = getJsonRequestBody(req);
        String login = params.get("login").getAsString();
        String password = params.get("password").getAsString();

        // Check validity
        if (!ValidationUtils.validateLogin(login)) {
            throw new ApiException(400, "invalidLogin", "Login did not match the specs");
        }
        if (!ValidationUtils.validateLogin(password)) {
            throw new ApiException(400, "invalidPassword", "Password did not match the specs");
        }

        // Get user from login
        User user = UsersRepository.getUserByLogin(login);
        if (user != null) {
            String hash = DigestUtils.sha256Hex(password + user.id);
            if (hash.equals(user.password)) {
                return TokenUtils.generateToken(user.id);
            } else {
                throw new ApiException(403, "invalidPassword", "Incorrect password");
            }
        }else {
            throw new ApiException(404, "invalidLogin", "User not found");
        }

    }
}
