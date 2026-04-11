package lkt.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Ticket {
    private Integer ID;
    private String title;
    private String detail;
    private User creator;
    private State state;
    private Priority priority;
    private TicketType ticketType;
    private LocalDateTime timeCreated;
    private LocalDateTime timeProcessing;
    private LocalDateTime timeResolved;
    private User assignee;
    private String cause;
    private Comment result;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public LocalDateTime getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(LocalDateTime timeCreated) {
        this.timeCreated = timeCreated;
    }

    public LocalDateTime getTimeProcessing() {
        return timeProcessing;
    }

    public void setTimeProcessing(LocalDateTime timeProcessing) {
        this.timeProcessing = timeProcessing;
    }

    public LocalDateTime getTimeResolved() {
        return timeResolved;
    }

    public void setTimeResolved(LocalDateTime timeResolved) {
        this.timeResolved = timeResolved;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Comment getResult() {
        return result;
    }

    public void setResult(Comment result) {
        this.result = result;
    }
}
