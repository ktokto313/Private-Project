package lkt.service;

import lkt.model.Role;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.repository.IUserRepository;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public User getAccountByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean changePassword(Integer userId, String password, String newPassword) {
        User foundUser = userRepository.findByUserID(userId);
        if (foundUser == null) {
            return false;
        }

        if (!bCryptPasswordEncoder.matches(password, foundUser.getPassword())) {
            return false;
        }

        foundUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.update(foundUser);
        return true;
    }
}
