package lkt.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lkt.service.IAuthService;
import lkt.util.CookieUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;
    @Value("${jwt.cookie.name}")
    private String cookieName;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody User user) {
        try {
            String jwt = authService.login(user);
            //Todo remove debug
            System.out.println(user);
            ResponseCookie cookie = CookieUtil.makeCookieFromJWT(jwt);
            String stringBuilder = cookieName + '=' + cookie;
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, stringBuilder)
                    .build();
        } catch (Exception e) {
            //TODO remove debug
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Cookie authCookie = CookieUtil.getJWTCookie(request.getCookies());
        if (authCookie == null) {
            return ResponseEntity.badRequest()
                    .build();
        }
        ResponseCookie clearedCookie = CookieUtil.invalidateCookie(authCookie.getValue());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearedCookie.toString())
                .build();
    }
}
