package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "priority")
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer ID;

    @Column(name = "level", nullable = false)
    private int levelOfPriority;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "time_to_respond", nullable = false)
    private Duration timeToRespond;

    @Column(name = "time_to_finish", nullable = false)
    private Duration timeToFinish;

    @OneToMany(mappedBy = "priority", fetch = FetchType.LAZY)
    private Set<Ticket> tickets = new HashSet<>();

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public int getLevelOfPriority() {
        return levelOfPriority;
    }

    public void setLevelOfPriority(int levelOfPriority) {
        this.levelOfPriority = levelOfPriority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getTimeToRespond() {
        return timeToRespond;
    }

    public void setTimeToRespond(Duration timeToRespond) {
        this.timeToRespond = timeToRespond;
    }

    public Duration getTimeToFinish() {
        return timeToFinish;
    }

    public void setTimeToFinish(Duration timeToFinish) {
        this.timeToFinish = timeToFinish;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
}
