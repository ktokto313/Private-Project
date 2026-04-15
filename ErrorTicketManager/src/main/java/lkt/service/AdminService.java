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
    public Priority createPriority(Priority priority) {
        if (priority == null) {
            return null;
        }
        if (priority.getName() == null || priority.getName().isBlank()) {
            return null;
        }
        if (priority.getTimeToRespond() == null || priority.getTimeToRespond().isNull()){
            return null;
        }
        if (priority.getTimeToFinish() == null || priority.getTimeToFinish().isNull()) {
            return null;
        }
        priority.setName(priority.getName().trim());
        return priorityRepository.insert(priority);
    }

    @Override
    public boolean changePriority(Priority priority) {
        if (priority.getID() == null || priority.getName() == null || priority.getName().isBlank() ||
                priority.getTimeToRespond() == null || priority.getTimeToRespond().isNull()||
                priority.getTimeToFinish() == null || priority.getTimeToFinish().isNull()) {
            return false;
        }
        priority.setName(priority.getName().trim());
        return priorityRepository.update(priority);
    }

    @Override
    public boolean deletePriority(Integer priorityID) {
        if (priorityID == null) {
            return false;
        }
        return priorityRepository.deleteByID(priorityID);
    }
}
