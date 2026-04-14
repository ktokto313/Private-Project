package lkt.repository;

import lkt.model.Priority;

import java.util.List;

public interface IPriorityRepository {
    List<Priority> findAll();

    boolean update(Integer priorityID, int levelOfPriority, String name, String timeToRespond, String timeToFinish);

    boolean deleteByID(Integer priorityID);
}
