package lkt.service;

import lkt.model.Priority;
import lkt.model.Ticket;
import lkt.model.User;

import java.util.List;

public interface IAdminService {
    boolean changeUserPassword(Integer userID, String newPassword);

    boolean changeDepartment(Integer userID, Integer department);

    boolean deleteAccount(Integer userID);

    List<Priority> getPriorities();

    Priority createPriority(Priority priority);

    boolean changePriority(Priority priority);

    boolean deletePriority(Integer priorityID);

    boolean updateTicket(Integer ticketID, Ticket modifiedTicket, User authenticatedUser);
}
