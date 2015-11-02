package fr.ecp.sio.jablog;

import fr.ecp.sio.jablog.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.security.Key;

/**
 * Created by charpi on 02/11/15.
 */
public class TokenUtils {

    private static final Key KEY = MacProvider.generateKey();

    public static String generateToken(Long id){
       return Jwts.builder()
               .setId(Long.toString(id))
               .signWith(SignatureAlgorithm.HS512, KEY)
               .compact();
    }

    public static long parseToken(String token) throws SignatureException{

            return Long.parseLong(Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getId());

    }
}
