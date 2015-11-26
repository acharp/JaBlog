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
            Object response = doGet(req); // Récupération de la requête via la fct doGet ci dessous implémentée par les sous-classes servlet
            sendResponse(response,resp);
        } catch (ApiException e) {
            resp.setStatus(e.getError().status);
            sendResponse(e, resp);
        }
    }
    // On a placé le mot-clé final sur cette méthode pour interdire aux classes filles d'overrider cette méthode
    // Qu'elles ne se trompent pas avec la doGet ci dessous qui est la méthode à implémenter par les classes filles.

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
            return null;
        }
    }

    // On récupère les paramètres sans préciser le type. En utilisant JsonParser et en récupérant un simple objet Json.
    protected static JsonObject getJsonParameters(HttpServletRequest req) throws IOException {
        return new JsonParser()
                .parse(
                        new InputStreamReader(req.getInputStream())
                ).getAsJsonObject();
    }

    // Pareil que ci-dessus mais variante en précisant le type en paramètre.
    // On utilise Gson avec son fromJson et on récupère un objet du type indiqué en paramètre.
    // Plus simple pour instancier directement un objet de notre modèle depuis un json.
    protected static <T> T getJsonParameters(HttpServletRequest req, Class<T> type) throws IOException{
        return GsonFactory.getGson().fromJson(
                new InputStreamReader(req.getInputStream()),
                type
        );
    }
}
