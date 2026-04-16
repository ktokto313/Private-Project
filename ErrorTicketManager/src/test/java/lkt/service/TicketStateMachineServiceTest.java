package lkt.service;

import lkt.model.Role;
import lkt.model.State;
import lkt.model.Ticket;
import lkt.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TicketStateMachineServiceTest {

    private final TicketStateMachineService machine = new TicketStateMachineService();

    private static User user(Integer id, Role role) {
        User u = new User();
        u.setUserID(id);
        u.setRole(role);
        return u;
    }

    private static Ticket ticketWithCreator(User creator) {
        Ticket t = new Ticket();
        t.setCreator(creator);
        return t;
    }

    @Test
    void creatorReopeningResolvedToProcessing_isAllowed() {
        User creator = user(1, Role.USER);
        User assignee = user(2, Role.IT);
        Ticket t = ticketWithCreator(creator);
        t.setAssignee(assignee);
        assertTrue(machine.isTransitionAllowed(State.RESOLVED, State.PROCESSING, creator, t));
    }

    @Test
    void creatorClosingResolvedToDone_isAllowed() {
        User creator = user(1, Role.IT);
        User assignee = user(2, Role.ADMIN);
        Ticket t = ticketWithCreator(creator);
        t.setAssignee(assignee);
        assertTrue(machine.isTransitionAllowed(State.RESOLVED, State.DONE, creator, t));
    }

    @Test
    void assigneeProcessingToFinish_isAllowed() {
        User creator = user(1, Role.IT);
        User assignee = user(2, Role.IT);
        Ticket t = ticketWithCreator(creator);
        t.setAssignee(assignee);
        assertTrue(machine.isTransitionAllowed(State.PROCESSING, State.RESOLVED, assignee, t));
    }

    @Test
    void creatorProcessingToFinish_isAllowed() {
        User creator = user(1, Role.IT);
        User assignee = user(2, Role.IT);
        Ticket t = ticketWithCreator(creator);
        t.setAssignee(assignee);
        assertTrue(machine.isTransitionAllowed(State.PROCESSING, State.RESOLVED, assignee, t));
    }

    @Test
    void nonAssigneeProcessingToResolve_throw() {
        User creator = user(1, Role.IT);
        User assignee = user(2, Role.IT);
        User requester = user(3, Role.IT);
        Ticket t = ticketWithCreator(creator);
        t.setAssignee(assignee);
        assertThrows(
                InvalidStateTransitionException.class,
                () -> machine.isTransitionAllowed(State.PROCESSING, State.RESOLVED, requester, t)
        );
    }

    @Test
    void nonCreatorReopeningResolvedToProcessing_throws() {
        User creator = user(1, Role.USER);
        User requester = user(2, Role.USER);
        Ticket t = ticketWithCreator(creator);

        assertThrows(
                InvalidStateTransitionException.class,
                () -> machine.isTransitionAllowed(State.RESOLVED, State.PROCESSING, requester, t)
        );
    }

    @Test
    void nonCreatorAdminOnlyTransition_throws() {
        User creator = user(1, Role.USER);
        User requester = user(2, Role.USER); // non-admin
        Ticket t = ticketWithCreator(creator);

        assertThrows(
                InvalidStateTransitionException.class,
                () -> machine.isTransitionAllowed(State.CREATED, State.PROCESSING, requester, t)
        );
    }

    @Test
    void adminOverride_AllTransitionsAllowed() {
        User admin = user(99, Role.ADMIN);

        // Not creator, ticket is irrelevant for admin override.
        assertTrue(machine.isTransitionAllowed(State.PROCESSING, State.CREATED, admin, null));
    }

    @Test
    void sameStateTransition_isRejectedForNonAdmin() {
        User creator = user(1, Role.USER);
        Ticket t = ticketWithCreator(creator);

        assertThrows(
                InvalidStateTransitionException.class,
                () -> machine.isTransitionAllowed(State.RESOLVED, State.RESOLVED, creator, t)
        );
    }

    @Test
    void nullStateInputs_throw() {
        User creator = user(1, Role.USER);
        Ticket t = ticketWithCreator(creator);

        assertThrows(
                InvalidStateTransitionException.class,
                () -> machine.isTransitionAllowed(null, State.PROCESSING, creator, t)
        );
        assertThrows(
                InvalidStateTransitionException.class,
                () -> machine.isTransitionAllowed(State.RESOLVED, null, creator, t)
        );
    }
}

