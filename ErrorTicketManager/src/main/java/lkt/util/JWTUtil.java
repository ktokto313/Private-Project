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
    private static String secret;
    private static Algorithm algorithm;
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

    @Value("${jwt.secret}")
    private void setSecret(String secret) {
        JWTUtil.secret = secret;
        JWTUtil.algorithm = Algorithm.HMAC256(secret);
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
