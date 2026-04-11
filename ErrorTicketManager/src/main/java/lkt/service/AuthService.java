package lkt.service;

import lkt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.repository.IUserRepository;
import lkt.util.JWTUtil;

import java.util.List;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private IUserRepository userRepository;
    // Create default bCrypt encoder with strength 10
    // TODO: tweak this to achieve ~1 sec verify time
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String login(User user) {
        //TODO admin injection to test :pensive:
        //TODO must remove, don't backdoor me
        if (user.getUsername().equals("sigma") && user.getPassword().equals("dongnai")) {
            return JWTUtil.createToken(user);
        }

        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null ||
                !bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword()))
            return null;
        return JWTUtil.createToken(user);
    }
}
