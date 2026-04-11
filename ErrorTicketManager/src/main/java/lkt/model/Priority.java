package lkt.model;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class Priority {
    private Integer ID;
    private int levelOfPriority;
    private String name;
    private Duration timeToRespond;
    private Duration timeToFinish;

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
}
