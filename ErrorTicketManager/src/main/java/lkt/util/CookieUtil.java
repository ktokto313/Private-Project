package lkt.util;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    private static Long cookieMaxAge;
    private static String cookieName;

    @Value("${jwt.lifetime}")
    private void setCookieMaxAge(Long cookieMaxAge) {
        CookieUtil.cookieMaxAge = cookieMaxAge;
    }

    @Value("${jwt.cookie.name}")
    private void setCookieName(String cookieName) {
        CookieUtil.cookieName = cookieName;
    }

    public static ResponseCookie makeCookieFromJWT(String jwt) {
        return ResponseCookie.from(cookieName)
                .value(jwt)
                .httpOnly(true)
                .path("/")
                .secure(true)
                .sameSite("Strict")
                .maxAge(cookieMaxAge)
                .build();
    }

    public static ResponseCookie invalidateCookie(String cookie) {
        return ResponseCookie.from(cookieName)
                .value("")
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("Strict")
                .maxAge(0)
                .build();
    }

    public static Cookie getJWTCookie(Cookie[] cookies) {
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) return cookie;
        }
        return null;
    }
}
