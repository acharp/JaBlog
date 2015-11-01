package fr.ecp.sio.jablog.api;

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
        Object response = doGet(req);
        // Write to response
    }

    protected Object doGet(HttpServletRequest req) throws ServletException, IOException{
        return null;
    }

    protected void sendResponse( Object T, HttpServletResponse response){

    }

}
