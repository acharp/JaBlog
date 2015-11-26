package fr.ecp.sio.jablog.api;

import com.google.appengine.repackaged.com.google.gson.Gson;
import fr.ecp.sio.jablog.data.UsersRepository;
import fr.ecp.sio.jablog.model.User;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by charpi on 30/10/15.
 */
public class UserServlet extends JsonServlet{

    @Override
    protected Object doGet(HttpServletRequest req) throws ServletException, IOException, ApiException {
        // Check user authentication
        User auth_user = getAuthenticatedUser(req);

        // Retrieve id of the needed user in the url
        String string_id = req.getRequestURI().split("/")[2];
        long id = Integer.parseInt(string_id);

        // Return user login jsonified
        Gson gson = new Gson();
        return gson.toJson(UsersRepository.getUser(id).login);

    }

    @Override
    protected Object doPost(HttpServletRequest req) throws ServletException, IOException, ApiException {
        User auth_user = getAuthenticatedUser(req);

        // Voir ce qu'on passe dans le json de cette requête : juste certains champs ou tout un user ?
        // juste certains champs => le mettre dans un dictionnaire puis selon contenu du dic modifier l'user

        // Pour update, il faudra probablement utiliser le save d'objectify, extrait de la doc :
        /*
        save
Saver save()
Start a save command chain. Allows you to save (or re-save) entity objects. Note that all command chain objects are immutable.

Saves do NOT cascade; if you wish to save an object graph, you must save each individual entity.

A quick example: ofy().save().entities(e1, e2, e3).now();

All command objects are immutable; this method returns a new object rather than modifying the current command object.

Returns:
the next step in the immutable command chain.
         */

        return null;
    }

    @Override
    protected Object doDelete(HttpServletRequest req) throws ServletException, IOException {
        return null;
    }
}
