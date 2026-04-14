package lkt.service;

import lkt.model.*;
import lkt.repository.ITicketRepository;
import lkt.repository.IUserRepository;
import lkt.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TicketService implements ITicketService {
    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean createTicket(
            String title,
            String description,
            Integer priorityID,
            Integer typeID,
            byte[] fileContent,
            String fileContentType,
            User authenticatedUser
    ) {
        if (authenticatedUser == null || authenticatedUser.getUserID() == null) {
            return false;
        }
        if (isBlank(title) || isBlank(description) || priorityID == null) {
            return false;
        }

        Integer ticketID = ticketRepository.insertTicket(
                title.trim(),
                description.trim(),
                authenticatedUser.getUserID(),
                priorityID,
                typeID
        );
        if (ticketID == null) {
            return false;
        }

        if (fileContent == null || fileContent.length == 0) {
            return true;
        }
        return ticketRepository.insertAttachment(fileContent, fileContentType, AttachmentType.TICKET.toString(), ticketID);
    }

    @Override
    public List<Ticket> getTickets(User authenticatedUser) {
        if (authenticatedUser == null || authenticatedUser.getUserID() == null) {
            return Collections.emptyList();
        }
        return ticketRepository.findAccessibleTickets(
                authenticatedUser.getUserID(),
                canAccessAllTickets(authenticatedUser)
        );
    }

    @Override
    public Ticket viewTicket(Integer ticketID, User authenticatedUser) {
        if (ticketID == null || authenticatedUser == null || authenticatedUser.getUserID() == null) {
            return null;
        }
        return ticketRepository.findAccessibleTicketByID(
                ticketID,
                authenticatedUser.getUserID(),
                canAccessAllTickets(authenticatedUser)
        );
    }

    @Override
    public boolean updateTicketStatus(Integer ticketID, String statusCode, User authenticatedUser) {
        if (!canManageTicket(authenticatedUser)) {
            return false;
        }
        State state = Util.getStateFromString(statusCode);
        if (ticketID == null || state == null) {
            return false;
        }
        Ticket ticket = viewTicket(ticketID, authenticatedUser);
        if (ticket == null) {
            return false;
        }
        return ticketRepository.updateTicketStatus(ticketID, state.toString());
    }

    @Override
    public boolean addAssignee(Integer ticketID, Integer userID, User authenticatedUser) {
        if (!canManageTicket(authenticatedUser) || ticketID == null || userID == null) {
            return false;
        }
        Ticket ticket = viewTicket(ticketID, authenticatedUser);
        User assignee = userRepository.findByUserID(userID);
        if (ticket == null || assignee == null) {
            return false;
        }
        return ticketRepository.updateAssignee(ticketID, userID);
    }

    @Override
    public boolean addComment(
            Integer ticketID,
            String detail,
            byte[] fileContent,
            String fileContentType,
            User authenticatedUser
    ) {
        if (ticketID == null || authenticatedUser == null || authenticatedUser.getUserID() == null) {
            return false;
        }
        if (isBlank(detail) && (fileContent == null || fileContent.length == 0)) {
            return false;
        }

        Ticket ticket = viewTicket(ticketID, authenticatedUser);
        if (ticket == null) {
            return false;
        }

        Integer commentID = ticketRepository.insertComment(
                isBlank(detail) ? null : detail.trim(),
                authenticatedUser.getUserID(),
                ticketID
        );
        if (commentID == null) {
            return false;
        }

        if (fileContent == null || fileContent.length == 0) {
            return true;
        }
        return ticketRepository.insertAttachment(fileContent, fileContentType, AttachmentType.COMMENT.toString(), commentID);
    }

    private boolean canAccessAllTickets(User user) {
        return user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.IT);
    }

    private boolean canManageTicket(User user) {
        return canAccessAllTickets(user);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
