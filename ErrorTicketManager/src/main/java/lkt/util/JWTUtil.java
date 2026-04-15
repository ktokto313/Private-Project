package lkt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JWTUtil {
    //TODO: SECRET is for developing use only, not for production, change this
    private static final String SECRET = "a-string-for-testing";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    //TODO: increase security of jwt
    private static String jwtIssuer;
    private static long lifetime;
    private static String cookieName;

    @Value("${jwt.cookie.name}")
    private void setCookieName(String cookieName) {
        JWTUtil.cookieName = cookieName;
    }

    @Value("${jwt.issuer}")
    private void setJwtIssuer(String issuer) {
        JWTUtil.jwtIssuer = issuer;
    }

    @Value("${jwt.lifetime}")
    private void setLifetime(Long lifetime) {
        JWTUtil.lifetime = lifetime;
    }

    public static String createToken(User user) {
        //TODO remove debug
        String debug = JWT.create()
            .withIssuer(jwtIssuer)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(lifetime))
            .withClaim("user", JacksonUtil.parseObjectToJSONString(user))
            .sign(ALGORITHM);
        System.out.println(debug);
        return debug;
    }

    public static DecodedJWT verify(String jwt) {
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM)
                .withIssuer(jwtIssuer)
                .withClaimPresence("user")
                //TODO recheck the leeway meaning
                //.acceptIssuedAt(lifetime)
                .build();
        return jwtVerifier.verify(jwt);
    }

    public static User getUser(HttpServletRequest request) {
        DecodedJWT decodedJWT = (DecodedJWT) request.getAttribute(cookieName);
        try {
            Claim claim = decodedJWT.getClaim("user");

            return JacksonUtil.parseJSONToObject(claim.asString(), User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
