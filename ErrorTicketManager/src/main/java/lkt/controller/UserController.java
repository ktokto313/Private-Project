package lkt.controller;

import jakarta.servlet.http.HttpServletRequest;
import lkt.model.User;
import lkt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lkt.service.IUserService;
import tools.jackson.databind.node.ObjectNode;

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

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changeUserPassword(
            @PathVariable Integer id,
            @RequestBody ObjectNode jsonNodes,
            HttpServletRequest request
    ) {
        User authenticatedUser = getUser(request);
        if (authenticatedUser == null ||
                authenticatedUser.getUserID() == null ||
                !authenticatedUser.getUserID().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean updated = userService.changePassword(id, jsonNodes.get("password").asString(), jsonNodes.get("newPassword").asString());
        if (!updated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    private User getUser(HttpServletRequest request) {
        return JWTUtil.getUser(request);
    }
}
