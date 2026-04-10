package controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;
import jakarta.servlet.http.HttpServletRequest;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.IUserService;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private IUserService userService;

    @Value("${jwt.cookie.name}")
    private String cookieName;

    @GetMapping("/me")
    public ResponseEntity<User> getLoggedInUserInfo(HttpServletRequest request) {
        User user = getUser(request);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user.getUserNoPassword());
    }

    @PostMapping
    public ResponseEntity<User> addAccount(
            @RequestParam String username,
            @RequestParam String password
    ) {
        try {
            User created = userService.addAccount(username, password);
            return ResponseEntity.status(HttpStatus.CREATED).body(created.getUserNoPassword());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getAccount(@PathVariable Integer id) {
        User user = userService.getAccount(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user.getUserNoPassword());
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changeUserPassword(
            @PathVariable Integer id,
            @RequestParam String password,
            @RequestParam String newPassword,
            HttpServletRequest request
    ) {
        User authenticatedUser = getUser(request);
        if (authenticatedUser == null ||
                authenticatedUser.getUserID() == null ||
                !authenticatedUser.getUserID().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean updated = userService.changePassword(id, password, newPassword);
        if (!updated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    private User getUser(HttpServletRequest request) {
        DecodedJWT decodedJWT = (DecodedJWT) request.getAttribute(cookieName);
        return decodedJWT.getClaim("user").as(User.class);
    }
}
