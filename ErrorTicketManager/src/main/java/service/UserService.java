package service;

import model.Role;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public User addAccount(String username, String password) {
        List<User> existingUsers = userRepository.findByUsername(username);
        if (!existingUsers.isEmpty()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole(Role.USER);
        return userRepository.upsert(user);
    }

    @Override
    public User getAccount(Integer id) {
        return userRepository.findByUserID(id);
    }

    @Override
    public User getAccountByUsername(String username) {
        List<User> foundUsers = userRepository.findByUsername(username);
        if (foundUsers.isEmpty()) {
            return null;
        }
        return foundUsers.get(0);
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
        userRepository.upsert(foundUser);
        return true;
    }
}
