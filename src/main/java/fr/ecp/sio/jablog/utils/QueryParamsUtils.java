package fr.ecp.sio.jablog.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by charpi on 17/12/15.
 */
public class QueryParamsUtils {

    public static Map<String, String> getQueryParams(HttpServletRequest req) {
        String queryString = req.getQueryString();
        if (queryString != null) {

            String[] params = queryString.split("&");
            Map<String, String> paramsDic = new HashMap<>();
            for (String param : params) {
                String name = param.split("=")[0];
                String value = param.split("=")[1];
                paramsDic.put(name, value);
            }

            return paramsDic;
        }
        else return null;
    }
}
