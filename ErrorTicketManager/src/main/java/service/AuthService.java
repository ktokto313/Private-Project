package service;

import com.auth0.jwt.JWT;
import model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import util.JWTUtil;

@Service
public class AuthService implements IAuthService {
    private UserRepository userRepository;
    // Create default bCrypt encoder with strength 10
    // TODO: tweak this to achieve ~1 sec verify time
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String login(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null ||
                bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword()))
            return null;
        return JWTUtil.createToken(user);
    }
}
