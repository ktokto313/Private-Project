package lkt.service;

import lkt.model.*;

import java.util.List;

public interface IAdminService {
    User addAccount(String username, String password);

    User getAccount(String username);

    List<User> getAccountsByRole(Role role);

    boolean changeUserPassword(Integer userID, String newPassword);

    List<Department> getDepartments();

    boolean changeDepartment(Integer userID, Integer department);

    boolean deleteAccount(Integer userID);

    List<Priority> getPriorities();

    Priority createPriority(Priority priority);

    boolean changePriority(Priority priority);

    boolean deletePriority(Integer priorityID);

    boolean updateTicket(Integer ticketID, Ticket modifiedTicket, User authenticatedUser);
}
