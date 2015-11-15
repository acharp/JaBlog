package fr.ecp.sio.jablog.api;

import fr.ecp.sio.jablog.model.Error;

/**
 * Created by charpi on 02/11/15.
 */
public class ApiException extends Exception {

    private Error mError;

    public ApiException(int status, String code, String message) {
        super(message);
        mError = new Error();
        mError.status = status;
        mError.code = code;
        mError.message = message;
    }

    public Error getError() {
        return mError;
    }
}
