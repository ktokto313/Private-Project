package service;

import com.auth0.jwt.JWT;
import model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import util.JWTUtil;

import java.util.List;

@Service
public class AuthService implements IAuthService {
    private UserRepository userRepository;
    // Create default bCrypt encoder with strength 10
    // TODO: tweak this to achieve ~1 sec verify time
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String login(User user) {
        //TODO admin injection to test :pensive:
        //TODO must remove, don't backdoor me
        if (user.getUsername().equals("sigma") && user.getPassword().equals("dongnai")) {
            return JWTUtil.createToken(user);
        }

        List<User> foundUsers = userRepository.findByUsername(user.getUsername());
        if (foundUsers.isEmpty() ||
                !bCryptPasswordEncoder.matches(user.getPassword(), foundUsers.get(0).getPassword()))
            return null;
        return JWTUtil.createToken(user);
    }
}
