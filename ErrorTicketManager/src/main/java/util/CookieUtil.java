package util;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;

public class CookieUtil {
    @Value("${jwt.lifeTime}")
    private static Long cookieMaxAge;
    @Value("${jwt.cookie.name}")
    private static String cookieName;

    public static ResponseCookie makeCookieFromJWT(String jwt) {
        return ResponseCookie.from(jwt)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(cookieMaxAge)
                .build();
    }

    public static ResponseCookie invalidateCookie(String cookie) {
        return ResponseCookie.from(cookie)
                .httpOnly(true)
                .maxAge(0)
                .build();
    }

    public static Cookie getJWTCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) return cookie;
        }
        return null;
    }
}
