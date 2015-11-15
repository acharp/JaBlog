package fr.ecp.sio.jablog.api;

import fr.ecp.sio.jablog.data.MessagesRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
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
        return MessagesRepository.getMessage(id);
    }

    @Override
    protected Object doPost(HttpServletRequest req) throws ServletException, IOException {
        return null;
    }

    @Override
    protected Object doDelete(HttpServletRequest req) throws ServletException, IOException {
        return null;
    }

}
