package fr.ecp.sio.jablog.api;

import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by charpi on 10/12/15.
 */
public class FollowedServlet extends JsonServlet {

    @Override
    protected List<User> doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        long id = 0;
        int limit = 0;

        //return UsersRepository.getUserFollowed(id,limit).users;
        return null;

    }
}
