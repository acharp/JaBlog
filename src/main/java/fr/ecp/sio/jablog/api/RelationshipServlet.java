package fr.ecp.sio.jablog.api;

import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.data.UsersRepository.UsersList;
import fr.ecp.sio.jablog.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by charpi on 10/12/15.
 */
public class RelationshipServlet extends JsonServlet {

    private final static String FOLLOWERSOF = "FollowersOf";
    private final static String FOLLOWEDBY = "FollowedBy";

    @Override
    protected UsersList doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {

        String uri = req.getRequestURI();
        String test = adaptURI(uri,req);

        long id = Long.parseLong(uri.split("/")[2]);
        if (UsersRepository.getUser(id) == null) {
            throw new ApiException(404, "Resource not found", "User doesn't exist");
        }

        // Params default values
        int limit = 20;
        String cursor = null;

        // Retrieve query string params
        String queryString = req.getQueryString();
        if (queryString != null){

            String[] params = queryString.split("&");
            Map<String, String> paramsDic = new HashMap<>();
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                paramsDic.put(name, value);
            }

            if (paramsDic.containsKey("limit")) {
                limit = Integer.parseInt(paramsDic.get("limit"));
            }
            if (paramsDic.containsKey("cursor")) {
                cursor = paramsDic.get("cursor");
            }
        }

        // Get following relationship infos. Getting methods depend on the existence of cursor param.
        UsersList result = null;
        if (cursor != null) {

            if (uri.contains(FOLLOWEDBY)) {
                result = UsersRepository.getUserFollowed(cursor, limit);
            }
            else if (uri.contains(FOLLOWERSOF)) {
                result = UsersRepository.getUserFollowers(cursor, limit);
            }
            return handleCursor(result, limit);
        }
        else {
            if (uri.contains(FOLLOWEDBY)) {
                result = UsersRepository.getUserFollowed(id, limit);
            }
            else if (uri.contains(FOLLOWERSOF)) {
                result = UsersRepository.getUserFollowers(id, limit);
            }
            return handleCursor(result, limit);
        }
    }

    private UsersList handleCursor(UsersList result, int limit) {
        if (result.users.size() < limit) {
            result.cursor = null;
            return result;
        } else {
            // TODO: implement a real cursor handling
            result.cursor = "nextCursor";
            return result;
        }
    }

    private String adaptURI(String uri, HttpServletRequest req) throws ApiException {

        if (uri.contains("me")) {
            User auth_user = getAuthenticatedUser(req);
            Long userid = auth_user.id;
            String[] uritab = uri.split("/");
            String new_uri = uritab[0] + userid + uritab[2];
            return new_uri;
        }
        return uri;
    }
}
