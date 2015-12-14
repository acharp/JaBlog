package fr.ecp.sio.jablog.api;


import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.User;
import fr.ecp.sio.jablog.utils.MD5Utils;
import fr.ecp.sio.jablog.utils.TokenUtils;
import fr.ecp.sio.jablog.utils.ValidationUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by charpi on 30/10/15.
 */
public class UsersServlet extends JsonServlet {

    @Override
    protected List<User> doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // TODO: define parameters to search/filter users by login, with limit, order...
        // TODO: define parameters to get the followings and the followers of a user given its id
        return UsersRepository.getUsers().users;
    }

    @Override
    protected String doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {

        User user = getJsonRequestBody(req, User.class);
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
        user.id = UsersRepository.allocateNewId();

        // Default avatar, used while the user hasn't set his own with AvatarServlet
        user.avatar ="http://www.gravatar.com/avatar" + MD5Utils.md5Hex(user.email) + "?d=wavatar";

        // Hash password
        user.password = DigestUtils.sha256Hex(user.password + user.id);

        // Save user
        UsersRepository.saveUser(user);

        // Return a token
        return TokenUtils.generateToken(user.id);
    }
}
