package lkt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.repository.IUserRepository;
import lkt.model.User;

@Service
public class AdminService implements IAdminService {
    @Autowired
    private IUserRepository userRepository;

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public boolean changeUserPassword(Integer userID, String newPassword) {
        User user = userRepository.findByUserID(userID);
        if (user == null) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.update(user);
        return true;
    }

    @Override
    public boolean changeDepartment(Integer userID, Integer department) {
        User user = userRepository.findByUserID(userID);
        if (user == null) {
            return false;
        }
        user.setDepartmentID(department);
        userRepository.update(user);
        return true;
    }

    @Override
    public boolean deleteAccount(Integer userID) {
        User user = userRepository.findByUserID(userID);
        if (user == null) {
            return false;
        }
        return userRepository.delete(user);
    }
}
