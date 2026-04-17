package lkt.service;

import lkt.model.Priority;
import lkt.model.Role;
import lkt.model.Ticket;
import lkt.model.User;

import java.util.List;

public interface IAdminService {
    User addAccount(String username, String password);

    User getAccount(int id);

    List<User> getAccountsByRole(Role role);

    boolean changeUserPassword(Integer userID, String newPassword);

    boolean changeDepartment(Integer userID, Integer department);

    boolean deleteAccount(Integer userID);

    List<Priority> getPriorities();

    Priority createPriority(Priority priority);

    boolean changePriority(Priority priority);

    boolean deletePriority(Integer priorityID);

    boolean updateTicket(Integer ticketID, Ticket modifiedTicket, User authenticatedUser);
}
