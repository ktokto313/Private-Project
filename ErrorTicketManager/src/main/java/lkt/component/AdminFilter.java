package lkt.component;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lkt.model.Role;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lkt.util.ResponseUtil;

import java.io.IOException;

@Component
public class AdminFilter extends OncePerRequestFilter {
    @Value("${jwt.cookie.name}")
    private String cookieName;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/admin");
    }

    @Override
    @Order(2)
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        DecodedJWT decodedJWT = (DecodedJWT) request.getAttribute(cookieName);
        if (decodedJWT == null) {
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED);
            return;
        }

        User user = decodedJWT.getClaim("user").as(User.class);
        if (user == null || user.getRole() != Role.ADMIN) {
            ResponseUtil.writeErrorResponse(response, HttpStatus.FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
