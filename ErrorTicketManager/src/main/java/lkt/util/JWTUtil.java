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
    private static Algorithm algorithm;
    private static String jwtIssuer;
    private static long lifetime;
    private static String cookieName;

    private JWTUtil() {}

    static {
        cookieName = System.getenv("JWT_COOKIE_NAME");
        jwtIssuer = System.getenv("JWT_ISSUER");
        lifetime = Long.parseLong(System.getenv("JWT_LIFETIME"));
        algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
    }

    public static String createToken(User user) {
        return JWT.create()
            .withIssuer(jwtIssuer)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusSeconds(lifetime))
            .withClaim("user", JacksonUtil.parseObjectToJSONString(user))
            .sign(algorithm);
    }

    public static DecodedJWT verify(String jwt) {
        JWTVerifier jwtVerifier = JWT.require(algorithm)
                .withIssuer(jwtIssuer)
                .withClaimPresence("user")
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
