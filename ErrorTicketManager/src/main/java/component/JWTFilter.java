package component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import util.CookieUtil;
import util.JWTUtil;
import util.ResponseUtil;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    @Value("${jwt.cookie.name}")
    private String cookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie cookie = CookieUtil.getJWTCookie(request.getCookies());
        if (cookie == null) {
            //TODO: check if verify throw exception or not
            ResponseUtil.writeErrorResponse(response, HttpStatus.UNAUTHORIZED);
            return;
        }
        JWTUtil.verify(cookie.getValue());
        filterChain.doFilter(request, response);
    }

    @Bean
    public FilterRegistrationBean<JWTFilter> loggingFilter(){
        FilterRegistrationBean<JWTFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JWTFilter());
        //TODO: add all the url pattern here
        registrationBean.addUrlPatterns("/api/*");

        return registrationBean;
    }
}
