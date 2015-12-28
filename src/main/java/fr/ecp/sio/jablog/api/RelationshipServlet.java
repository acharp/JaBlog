package fr.ecp.sio.jablog.api;

import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.data.UsersRepository.UsersList;
import fr.ecp.sio.jablog.model.User;
import fr.ecp.sio.jablog.utils.QueryParamsUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by charpi on 10/12/15.
 */
public class RelationshipServlet extends UsersServlet {

    private final static String FOLLOWERSOF = "FollowersOf";
    private final static String FOLLOWEDBY = "FollowedBy";

    @Override
    protected List<User> doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {

        String uri = req.getRequestURI();
        uri = UserServlet.adaptURI(uri,req);

        long id = Long.parseLong(uri.split("/")[2]);
        if (UsersRepository.getUser(id) == null) {
            throw new ApiException(404, "Resource not found", "User doesn't exist");
        }

        // Params default values
        Integer limit = 20;
        String cursor = null;

        // Retrieve query string params
        Map<String, String> paramsDic = QueryParamsUtils.getQueryParams(req);
        if (paramsDic != null) {
            if (paramsDic.containsKey("limit")) {
                limit = Integer.parseInt(paramsDic.get("limit"));
            }
            if (paramsDic.containsKey("cursor")) {
                cursor = paramsDic.get("cursor");
            }
        }

        // Get followers or followed list.
        UsersList result = null;
        if (uri.contains(FOLLOWEDBY)) {
            result = UsersRepository.getUserFollowed(id, cursor, limit);
        }
        else if (uri.contains(FOLLOWERSOF)) {
            result = UsersRepository.getUserFollowers(id, cursor, limit);
        }

        return handleCursor(result, limit).users;
    }

}
