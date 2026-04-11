package lkt.service;

import lkt.model.Role;
import lkt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.repository.IUserRepository;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public User addAccount(String username, String password) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole(Role.USER);
        return userRepository.insert(user);
    }

    @Override
    public User getAccount(Integer id) {
        return userRepository.findByUserID(id);
    }

    @Override
    public User getAccountByUsername(String username) {
        User foundUser = userRepository.findByUsername(username);
        if (foundUser == null) {
            return null;
        }
        return foundUser;
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
