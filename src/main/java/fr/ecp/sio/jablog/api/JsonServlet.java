package fr.ecp.sio.jablog.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.gson.GsonFactory;
import fr.ecp.sio.jablog.model.User;
import fr.ecp.sio.jablog.utils.TokenUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SignatureException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by charpi on 30/10/15.
 */
public class JsonServlet extends HttpServlet {

    protected static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("Bearer (.+)");

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object response = doGet(req);
            sendResponse(response,resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e, resp);
        }
    }

    protected Object doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        return null;
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object response = doPost(req);
            sendResponse(response,resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e, resp);
        }
    }

    protected Object doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        return null;
    }

    @Override
    protected final void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Object response = doDelete(req);
            sendResponse(response,resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e, resp);
        }
    }

    protected Object doDelete(HttpServletRequest req) throws ServletException, IOException, ApiException {
        return null;
    }

    private void sendResponse(Object response, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        GsonFactory.getGson().toJson(response, resp.getWriter());
    }

    protected static User getAuthenticatedUser(HttpServletRequest req) throws ApiException {
        String auth = req.getHeader("Authorization");
        if (auth != null) {
            Matcher m = AUTHORIZATION_PATTERN.matcher(auth);
            if (!m.matches()) {
                throw new ApiException(401, "invalidAuthorization", "Invalid authorization header format");
            }
            try {
                long id = TokenUtils.parseToken(m.group(1));
                return UsersRepository.getUser(id);
            } catch (SignatureException e) {
                throw new ApiException(401, "invalidAuthorization", "Invalid token");
            }

        } else {
            throw new ApiException(400, "invalidRequest", "Missing header authorization field");
        }
    }

    // Get Json input as JsonObject
    protected static JsonObject getJsonRequestBody(HttpServletRequest req) throws IOException {
        return new JsonParser()
                .parse(req.getReader())
                .getAsJsonObject();
    }

    // Get Json input to instanciate data model
    protected static <T> T getJsonRequestBody(HttpServletRequest req, Class<T> type) throws IOException {
        return GsonFactory.getGson().fromJson(req.getReader(), type);
    }
}
