package fr.ecp.sio.jablog.model;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

/**
 * Created by charpi on 30/10/15.
 */

@Entity
public class Message {

    @Id
    public Long id;

    public String text;
    public Date date;

    @Load
    public Ref<User> user;

}
