package fr.ecp.sio.jablog.api;


import com.googlecode.objectify.ObjectifyFactory;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.User;
import fr.ecp.sio.jablog.utils.TokenUtils;
import fr.ecp.sio.jablog.utils.ValidationUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by charpi on 30/10/15.
 */
public class UsersServlet extends JsonServlet {

    @Override
    protected Object doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User user = getAuthenticatedUser(req);

        List<User> users = UsersRepository.getUsers();
        List<String> usernames = new ArrayList<>();
        for (User u : users){
            usernames.add(u.login);
        }

        return usernames;
        //return gson.toJson(usernames, new TypeToken<List<String>>(){}.getType());
    }

    @Override
    protected String doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        User user = getJsonParameters(req, User.class);
        if (user == null){
            throw new ApiException(400, "invalidRequest", "Invalid JSON body");
        }


        if (!ValidationUtils.validateLogin(user.login)) {
            throw new ApiException(400, "invalidLogin", "Login did not match the specs");
        }

        if (!ValidationUtils.validatePassword(user.password)) {
            throw new ApiException(400, "invalidPassword", "Password did not match the specs");
        }

        if (!ValidationUtils.validateEmail(user.email)) {
            throw new ApiException(400, "invalidEmail", "Email did not match the specs");
        }

        if (UsersRepository.getUserByLogin(user.login) != null) {
            throw new ApiException(400, "duplicateLogin", "Duplicate login");
        }

        if (UsersRepository.getUserByEmail(user.email) != null) {
            throw new ApiException(400, "duplicateEmail", "Duplicate email");
        }


        // Explicitly give a fresh id to the user (we need it for next step)
        user.id = new ObjectifyFactory().allocateId(User.class).getId();

        // Hash password
        user.password = DigestUtils.sha256Hex(user.password + user.id);

        // Save user
        long id = UsersRepository.insertUser(user);

        // Return a token
        return TokenUtils.generateToken(id);
    }
}
