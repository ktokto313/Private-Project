package lkt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JWTUtil {
    //TODO: SECRET is for developing use only, not for production, change this
    private static final String SECRET = "tKPreUmrP&s;f=;hw)002Fr{6ZeY=pZ,_K&7rd_-CFb";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    //TODO: increase security of jwt
    @Value("${jwt.issuer}")
    private static String jwtIssuer;
    @Value("${jwt.lifetime}")
    private static long lifetime;

    public static String createToken(User user) {
        return JWT.create()
                .withIssuer(jwtIssuer)
                .withIssuedAt(Instant.now())
                .withClaim("user", user.toString())
                .sign(ALGORITHM);
    }

    public static DecodedJWT verify(String jwt) {
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM)
                .withIssuer(jwtIssuer)
                .withClaimPresence("user")
                .acceptIssuedAt(lifetime)
                .build();
        return jwtVerifier.verify(jwt);
    }
}
