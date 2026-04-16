package lkt.service;

import lkt.model.Role;
import lkt.model.State;
import lkt.model.Ticket;
import lkt.model.User;
import org.springframework.stereotype.Service;

@Service
public class TicketStateMachineService {

    /**
     * Checks whether the transition is allowed.
     *
     * @return {@code true} if allowed.
     * @throws InvalidStateTransitionException when not allowed.
     */
    public boolean isTransitionAllowed(
            State currentState,
            State targetState,
            User requestingUser,
            Ticket ticket
    ) {
        if (currentState == null || targetState == null) {
            throw new InvalidStateTransitionException("State transition must specify both current and target states.");
        }
        if (requestingUser == null) {
            throw new InvalidStateTransitionException(
                    "State transition requires a requesting user (got null)."
            );
        }
        if (requestingUser.getRole() == null) {
            throw new InvalidStateTransitionException(
                    "State transition requires requesting user role (requestingUser.role is null)."
            );
        }

        // Admin override: allow any state transition regardless of standard rules.
        if (requestingUser.getRole() == Role.ADMIN) {
            return true;
        }

        // From here on, only the ticket creator can reopen/close.
        if (ticket == null) {
            throw new InvalidStateTransitionException(
                    "State transition requires a ticket for non-admin users (got null ticket)."
            );
        }
        if (requestingUser.getUserID() == null) {
            throw new InvalidStateTransitionException(
                    "State transition requires requesting user id for non-admin users (requestingUser.userID is null)."
            );
        }
        if (ticket.getCreator() == null || ticket.getCreator().getUserID() == null) {
            throw new InvalidStateTransitionException(
                    "State transition requires ticket creator or assignee for non-admin users (ticket.creator.userID is null)."
            );
        }
        if (ticket.getAssignee() == null || ticket.getAssignee().getUserID() == null) {
            throw new InvalidStateTransitionException(
                    "State transition requires ticket creator or assignee for non-admin users (ticket.assignee.userID is null)."
            );
        }

        Integer requesterId = requestingUser.getUserID();
        Integer ticketCreatorId = ticket.getCreator().getUserID();
        Integer ticketAssigneeId = ticket.getAssignee().getUserID();

        boolean isCreator = ticketCreatorId.equals(requesterId);
        boolean isAssignee = ticketAssigneeId.equals(requesterId);
        //TODO
        System.out.println(isAssignee);
        System.out.println(isCreator);

        // Standard rule mapping (admin-only transitions are rejected for non-admin users).
        if (currentState == State.RESOLVED && targetState == State.PROCESSING) {
            if (isCreator) return true;
            throw new InvalidStateTransitionException(
                    String.format(
                            "Transition %s -> %s is allowed only for the ticket creator. Requester id=%s role=%s; ticket creator id=%s.",
                            currentState, targetState, requesterId, requestingUser.getRole(), ticketCreatorId
                    )
            );
        }

        if (currentState == State.RESOLVED && targetState == State.DONE) {
            if (isCreator) return true;
            throw new InvalidStateTransitionException(
                    String.format(
                            "Transition %s -> %s is allowed only for the ticket creator. Requester id=%s role=%s; ticket creator id=%s.",
                            currentState, targetState, requesterId, requestingUser.getRole(), ticketCreatorId
                    )
            );
        }

        if (currentState == State.PROCESSING && targetState == State.RESOLVED) {
            if (isAssignee || isCreator) return true;
            throw new InvalidStateTransitionException(
                    String.format(
                            "Transition %s -> %s is allowed only for the ticket assignee. Requester id=%s role=%s; ticket assignee id=%s.",
                            currentState, targetState, requesterId, requestingUser.getRole(), ticketCreatorId
                    )
            );
        }

        // Non-admin users are not allowed to perform the admin-only transitions, and any other transition is invalid.
        throw new InvalidStateTransitionException(
                String.format(
                        "Transition %s -> %s is not allowed for non-admin users. Requester id=%s role=%s; ticket creator id=%s. Allowed non-admin transitions: RESOLVED->PROCESSING and RESOLVED->DONE.",
                        currentState, targetState, requesterId, requestingUser.getRole(), ticketCreatorId
                )
        );
    }
}

