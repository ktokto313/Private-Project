package lkt.service;

import lkt.model.Ticket;
import lkt.model.User;

import java.util.List;

public interface ITicketService {
    boolean createTicket(
            String title,
            String description,
            Integer priorityID,
            Integer typeID,
            byte[] fileContent,
            String fileContentType,
            User authenticatedUser
    );

    List<Ticket> getTickets(User authenticatedUser);

    Ticket viewTicket(Integer ticketID, User authenticatedUser);

    boolean updateTicket(Integer ticketID, Ticket modifiedTicket, User authenticatedUser);

    boolean addAssignee(Integer ticketID, Integer userID, User authenticatedUser);

    boolean addComment(
            Integer ticketID,
            String detail,
            byte[] fileContent,
            String fileContentType,
            User authenticatedUser
    );
}
