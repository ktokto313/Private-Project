package lkt.service;

import lkt.model.*;
import lkt.observer.NotificationBroadcaster;
import lkt.repository.ITicketRepository;
import lkt.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TicketService implements ITicketService {
    @Autowired
    private ITicketRepository ticketRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private TicketStateMachineService ticketStateMachineService;

    @Autowired
    private NotificationBroadcaster notificationBroadcaster;

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
        if (ticketID != null) {
            List<User> adminList = userRepository.findUsersByRole(Role.ADMIN);
            notificationBroadcaster.notifySubscribers(authenticatedUser, adminList, State.CREATED.name(), ticketID, title, null);
        } else {
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
    public boolean updateTicketState(Integer ticketID, State state, User authenticatedUser) {
        if (ticketID == null || state == null) {
            return false;
        }
        Ticket baseTicket = viewTicket(ticketID, authenticatedUser);
        if (baseTicket == null) {
            return false;
        }
        if (!ticketStateMachineService.isTransitionAllowed(baseTicket.getState(), state, authenticatedUser, baseTicket)) {
            return false;
        }
        baseTicket.setState(state);
        boolean result = ticketRepository.updateTicket(ticketID, baseTicket);
        if (result) {
            List<User> receivingUserList = new ArrayList<>();
            receivingUserList.add(baseTicket.getAssignee());
            receivingUserList.add(baseTicket.getCreator());
            notificationBroadcaster.notifySubscribers(authenticatedUser, receivingUserList, state.name(), ticketID, baseTicket.getTitle(), null);
        }
        return result;
    }

// TODO disabled api line, migrated to admin APIs
//    @Override
//    public boolean addAssignee(Integer ticketID, Integer userID, User authenticatedUser) {
//        if (!canManageTicket(authenticatedUser) || ticketID == null || userID == null) {
//            return false;
//        }
//        Ticket ticket = viewTicket(ticketID, authenticatedUser);
//        User assignee = userRepository.findByUserID(userID);
//        if (ticket == null || assignee == null) {
//            return false;
//        }
//        return ticketRepository.updateAssignee(ticketID, userID);
//    }

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
        if (commentID != null) {
            List<User> receivingUserList = new ArrayList<>();
            receivingUserList.add(ticket.getAssignee());
            receivingUserList.add(ticket.getCreator());
            notificationBroadcaster.notifySubscribers(authenticatedUser, receivingUserList, "comment", ticketID, ticket.getTitle(), null);
        } else {
            return false;
        }

        if (fileContent == null || fileContent.length == 0) {
            return true;
        }
        return ticketRepository.insertAttachment(fileContent, fileContentType, AttachmentType.COMMENT.toString(), commentID);
    }

    private boolean canAccessAllTickets(User user) {
        return user != null && (user.getRole() == Role.ADMIN);
    }

    private boolean canManageTicket(User user) {
        return canAccessAllTickets(user);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
