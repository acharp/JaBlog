package fr.ecp.sio.jablog.api;

import fr.ecp.sio.jablog.MessageRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by charpi on 30/10/15.
 */
public class MessageServlet extends JsonServlet {

    @Override
    protected Object doGet(HttpServletRequest req) throws ServletException, IOException {
        // Récupérer l'id reçu par l'URL
        // Checker id
        // Chercher cet id dans notre repository
        // Convert to JSON
        // Send in response
        // Les 2 dernières vont être communes à tous les get de nos servlets donc ça vaut le coup de les délocaliser

        long id=0;
        return MessageRepository.getMessage(id);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

}
