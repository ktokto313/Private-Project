package lkt.repository;

import lkt.model.Ticket;

import java.util.List;

public interface ITicketRepository {
    Integer insertTicket(
            String title,
            String description,
            Integer creatorID,
            Integer priorityID,
            Integer ticketTypeID
    );

    boolean insertAttachment(byte[] content, String contentType, String attachedObjectType, Integer attachedObjectID);

    List<Ticket> findAccessibleTickets(Integer userID, boolean includeAllTickets);

    Ticket findAccessibleTicketByID(Integer ticketID, Integer userID, boolean includeAllTickets);

    boolean updateTicketStatus(Integer ticketID, Ticket ticket);

    boolean updateAssignee(Integer ticketID, Integer userID);

    Integer insertComment(String detail, Integer creatorID, Integer ticketID);
}
