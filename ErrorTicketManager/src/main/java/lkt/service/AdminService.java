package lkt.service;

import lkt.model.State;
import lkt.model.Ticket;
import lkt.repository.ITicketRepository;
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

    @Autowired
    private ITicketRepository ticketRepository;
    @Autowired
    private TicketStateMachineService ticketStateMachineService;

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

    @Override
    public boolean updateTicket(Integer ticketID, Ticket modifiedTicket, User authenticatedUser) {
        if (ticketID == null || modifiedTicket == null) {
            return false;
        }
        Ticket baseTicket = ticketRepository.findAccessibleTicketByID(ticketID, authenticatedUser.getUserID(), true);
        if (baseTicket == null) {
            return false;
        }
        // Only allow ticketType, assignees, priority, state change, purify data
        if (modifiedTicket.getTicketType() != null)
            baseTicket.setTicketType(modifiedTicket.getTicketType());
        if (modifiedTicket.getAssignee() != null)
            baseTicket.setAssignee(modifiedTicket.getAssignee());
        if (modifiedTicket.getPriority() != null)
            baseTicket.setPriority(modifiedTicket.getPriority());
        if (modifiedTicket.getState() != null) {
            if (!ticketStateMachineService.isTransitionAllowed(baseTicket.getState(), modifiedTicket.getState(), authenticatedUser, baseTicket)) {
                return false;
            }
            baseTicket.setState(modifiedTicket.getState());
        }

        return ticketRepository.updateTicket(ticketID, baseTicket);
    }
}
