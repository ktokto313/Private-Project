package lkt.service;

/**
 * Thrown when a ticket state transition is not allowed by the domain rules.
 */
public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(String message) {
        super(message);
    }
}

