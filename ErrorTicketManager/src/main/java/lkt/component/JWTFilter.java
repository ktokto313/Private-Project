package lkt.component;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lkt.util.CookieUtil;
import lkt.util.JWTUtil;
import lkt.util.ResponseUtil;

import java.io.IOException;

@Order(1)
@Component
public class JWTFilter extends OncePerRequestFilter {
    @Value("${jwt.cookie.name}")
    private String cookieName;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = CookieUtil.getJWTCookie(request.getCookies());
        if (cookie == null) {
            //TODO: check if verify throw exception or not
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED);
            return;
        }
        DecodedJWT decodedJWT = JWTUtil.verify(cookie.getValue());
        request.setAttribute(cookieName, decodedJWT);
        filterChain.doFilter(request, response);
    }
}
