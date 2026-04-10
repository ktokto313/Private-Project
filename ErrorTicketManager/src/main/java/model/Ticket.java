package model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer ID;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "detail", nullable = false)
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator", nullable = false)
    private User creator;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority", nullable = false)
    private Priority priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type", nullable = false)
    private TicketType ticketType;

    @Column(name = "time_created", nullable = false)
    private LocalDateTime timeCreated;

    @Column(name = "time_processing")
    private LocalDateTime timeProcessing;

    @Column(name = "time_resolved")
    private LocalDateTime timeResolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee")
    private User assignee;

    @Column(name = "cause")
    private String cause;

    @OneToOne
    private Comment result;

    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "attachable", fetch = FetchType.LAZY)
    private Set<Attachment> attachments;

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

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }
}
