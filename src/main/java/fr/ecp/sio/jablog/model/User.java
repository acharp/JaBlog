package fr.ecp.sio.jablog.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by charpi on 30/10/15.
 */

@Entity
public class User {

    @Id public long id;
    public String login;
    public String password;
    public String avatar;
    public String coverPicture;
    public String email;

}
