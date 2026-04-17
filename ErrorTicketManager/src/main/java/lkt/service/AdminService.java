package lkt.service;

import lkt.model.*;
import lkt.observer.NotificationBroadcaster;
import lkt.repository.ITicketRepository;
import lkt.repository.ITicketTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lkt.repository.IPriorityRepository;
import lkt.repository.IUserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService implements IAdminService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IPriorityRepository priorityRepository;

    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private ITicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketStateMachineService ticketStateMachineService;

    @Autowired
    private NotificationBroadcaster notificationBroadcaster;

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
    public User getAccount(int id) {
        return userRepository.findByUserID(id).getUserNoPassword();
    }

    @Override
    public List<User> getAccountsByRole(Role role) {
        List<User> userList = userRepository.findUsersByRole(role);
        if (userList == null) return null;
        userList.replaceAll(User::getUserNoPassword);
        return userList;
    }

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

    @Override
    public boolean updateTicket(Integer ticketID, Ticket modifiedTicket, User authenticatedUser) {
        if (ticketID == null || modifiedTicket == null) {
            return false;
        }
        Ticket baseTicket = ticketRepository.findAccessibleTicketByID(ticketID, authenticatedUser.getUserID(), true);
        if (baseTicket == null) {
            return false;
        }
        List<String> actions = new ArrayList<>();
        List<String> actionResults = new ArrayList<>();
        // Only allow ticketType, assignees, priority, state change, purify data
        if (modifiedTicket.getTicketType() != null) {
            baseTicket.setTicketType(modifiedTicket.getTicketType());
            actions.add("ticketType");
            actionResults.add(ticketTypeRepository.findByID(baseTicket.getTicketType().getID()).getTitle());
        }
        if (modifiedTicket.getAssignee() != null) {
            baseTicket.setAssignee(modifiedTicket.getAssignee());
            actions.add("assignee");
            actionResults.add(userRepository.findByUserID(baseTicket.getAssignee().getUserID()).getUsername());
        }
        if (modifiedTicket.getPriority() != null) {
            baseTicket.setPriority(modifiedTicket.getPriority());
            actions.add("priority");
            actionResults.add(priorityRepository.findByID(baseTicket.getPriority().getID()).getName());
        }
        if (modifiedTicket.getState() != null) {
            if (!ticketStateMachineService.isTransitionAllowed(baseTicket.getState(), modifiedTicket.getState(), authenticatedUser, baseTicket)) {
                return false;
            }
            baseTicket.setState(modifiedTicket.getState());
            actions.add(modifiedTicket.getState().name());
            actionResults.add(null);
        }

        boolean result = ticketRepository.updateTicket(ticketID, baseTicket);
        if (result) {
            for (int i = 0; i < actions.size(); i++) {
                List<User> receivingUserList = new ArrayList<>();
                receivingUserList.add(baseTicket.getAssignee());
                receivingUserList.add(baseTicket.getCreator());
                notificationBroadcaster.notifySubscribers(authenticatedUser, receivingUserList, actions.get(i), ticketID, baseTicket.getTitle(), actionResults.get(i));
            }
        }
        return result;
    }
}
