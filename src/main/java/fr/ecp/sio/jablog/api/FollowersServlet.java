package fr.ecp.sio.jablog.api;

import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.data.UsersRepository.UsersList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by charpi on 10/12/15.
 */
public class FollowersServlet extends JsonServlet{

    @Override
    protected UsersList doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {

        String string_id = req.getRequestURI().split("/")[2];
        long id = Long.parseLong(string_id);
        if (UsersRepository.getUser(id) == null) {
            throw new ApiException(404, "Resource not found", "User doesn't exist");
        }

        // If there isn't a Limit header, default value for limit is 20
        int limit = 20;
        if (req.getHeader("Limit") != null) {
            limit = (req.getIntHeader("Limit"));
        }

        String cursor = req.getHeader("Cursor");
        if (cursor != null) {

            if (!(cursor.length() > 0)) {
                throw new ApiException(400, "invalidRequest", "Header Cursor is empty");
            }

            UsersList result = UsersRepository.getUserFollowers(req.getHeader("Cursor"), limit);
            return FollowedServlet.handleCursor(result, limit);
        }
        else {
            UsersList result = UsersRepository.getUserFollowers(id, limit);
            return FollowedServlet.handleCursor(result, limit);
        }

    }

}
