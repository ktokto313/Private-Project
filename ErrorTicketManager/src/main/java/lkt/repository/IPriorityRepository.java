package lkt.repository;

import lkt.model.Priority;

import java.util.List;

public interface IPriorityRepository {
    List<Priority> findAll();

    Priority insert(Priority priority);

    boolean update(Priority priority);

    boolean deleteByID(Integer priorityID);
}
