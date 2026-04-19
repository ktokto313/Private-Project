package lkt.service;

import lkt.model.User;
import lkt.repository.IDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.repository.IUserRepository;
import lkt.util.JWTUtil;

@Service
public class AuthService implements IAuthService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IDepartmentRepository departmentRepository;
    // Create default bCrypt encoder with strength 10
    // TODO: tweak this to achieve ~1 sec verify time
    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public String login(User user) {
        User foundUser = userRepository.findByUsername(user.getUsername());
        if (foundUser == null ||
                !bCryptPasswordEncoder.matches(user.getPassword(), foundUser.getPassword()))
            return null;
        foundUser.setDepartment(departmentRepository.findByID(foundUser.getDepartmentID()));
        user.setUserID(foundUser.getUserID());
        user.setRole(foundUser.getRole());
        user.setDepartment(foundUser.getDepartment());
        return JWTUtil.createToken(foundUser.getUserNoPassword());
    }
}
