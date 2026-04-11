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
