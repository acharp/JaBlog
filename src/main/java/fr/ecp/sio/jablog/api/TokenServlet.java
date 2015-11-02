package fr.ecp.sio.jablog.api;

import com.google.appengine.repackaged.com.google.gson.JsonObject;

import com.google.appengine.repackaged.com.google.gson.JsonParser;
import fr.ecp.sio.jablog.ApiException;
import fr.ecp.sio.jablog.TokenUtils;
import fr.ecp.sio.jablog.UsersRepository;
import fr.ecp.sio.jablog.ValidationUtils;
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
    protected Object doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        // Extract login and password from request
        JsonObject params = new JsonParser()
                .parse(
                        new InputStreamReader(req.getInputStream())
                ).getAsJsonObject();
        String login = params.get("login").getAsString();
        String password = params.get("password").getAsString();

        // Check validity
        if (!ValidationUtils.validateLogin(login)) {
            throw new ApiException("Login did not match the specs", 404, "invalidLogin");
        }
        if (!ValidationUtils.validateLogin(password)) {
            throw new ApiException("Password did not match the specs", 404, "invalidPassword");
        }

        // Get user from login
        User user = UsersRepository.getUser(login);
        if (user != null) {
            // SHA 256 password (salt=id)
            // Le sel du hashage permet de garantir que même si deux users choisissent le même password, le hash de chacun de ces password sera
            // différent car chaque user a un id différent (et c'est parceque l'id est unique qu'on le choisit comme sel de hashage).
            // Check password (hash...)
            // Generate token
            String hash = DigestUtils.sha256Hex(password + user.id);
            if (hash.equals(user.password)) {
                // Pour générer le token on choisit d'encoder l'id de l'user. On fait cette encodage avec la librairie jjwt. cf TokenUtils
                return TokenUtils.generateToken(user.id);
            } else {
                throw new ApiException("Wrong password", 403, "XXXXXXXXX");
            }
        }else {
            throw new ApiException("User not found", 404, "invaliLogin");
        }

        return super.doPost(req);
    }
}
