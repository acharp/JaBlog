package fr.ecp.sio.jablog.api;

import com.google.gson.Gson;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by charpi on 30/10/15.
 */
public class JsonServlet extends HttpServlet {

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
            Object response = doGet(req);
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
            Object response = doGet(req);
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
        new Gson().toJson(response, resp.getWriter());
    }

    protected User getAuthenticatedUser(HttpServletRequest req){
        String auth = req.getHeader("Authorization");
        if (auth != null){
            // Check that auth is "Bearer {a token}". cf classe Pattern de java pour voir comment fctionne cette vérif.
            // Check token
            // Handle possible error
            // Get the id of the user
            long id = 2;
            return UsersRepository.getUser(id);
        } else {
            return null;
        }
    }

}
