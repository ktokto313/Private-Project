package lkt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.model.Priority;
import lkt.repository.IPriorityRepository;
import lkt.repository.IUserRepository;
import lkt.model.User;

import java.util.List;

@Service
public class AdminService implements IAdminService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPriorityRepository priorityRepository;

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

    @Override
    public List<Priority> getPriorities() {
        return priorityRepository.findAll();
    }

    @Override
    public boolean changePriority(Integer priorityID, int levelOfPriority, String name, String timeToRespond, String timeToFinish) {
        if (priorityID == null || name == null || name.isBlank() || timeToRespond == null || timeToRespond.isBlank() || timeToFinish == null || timeToFinish.isBlank()) {
            return false;
        }
        return priorityRepository.update(priorityID, levelOfPriority, name.trim(), timeToRespond.trim(), timeToFinish.trim());
    }

    @Override
    public boolean deletePriority(Integer priorityID) {
        if (priorityID == null) {
            return false;
        }
        return priorityRepository.deleteByID(priorityID);
    }
}
