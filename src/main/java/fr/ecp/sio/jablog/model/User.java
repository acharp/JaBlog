package fr.ecp.sio.jablog.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by charpi on 30/10/15.
 */

@Entity
public class User {

    @Id
    public long id;

    @Index
    public String login;

    @Index
    public String email;

    public String password;
    public String avatar;
    public String coverPicture;


}
