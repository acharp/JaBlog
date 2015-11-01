package fr.ecp.sio.jablog.api;

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
    // Ici on ne retourne pas une réponse mais on l'a en paramètre donc on va juste la modifier.
    protected Object doGet(HttpServletRequest req) throws ServletException, IOException {
        // super.doGet(req, resp);
        // Par défaut le doGet renvoie un erreur donc il ne faut pas faire cet appel à super.
        return null;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }
}
