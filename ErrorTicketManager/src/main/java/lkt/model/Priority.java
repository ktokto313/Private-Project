package lkt.model;

import org.postgresql.util.PGInterval;

public class Priority {
    private Integer id;
    private int levelOfPriority;
    private String name;
    private PGInterval timeToRespond;
    private PGInterval timeToFinish;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public PGInterval getTimeToRespond() {
        return timeToRespond;
    }

    public void setTimeToRespond(PGInterval timeToRespond) {
        this.timeToRespond = timeToRespond;
    }

    public PGInterval getTimeToFinish() {
        return timeToFinish;
    }

    public void setTimeToFinish(PGInterval timeToFinish) {
        this.timeToFinish = timeToFinish;
    }
}
