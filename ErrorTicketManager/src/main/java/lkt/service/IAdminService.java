package lkt.service;

import lkt.model.Priority;

import java.util.List;

public interface IAdminService {
    boolean changeUserPassword(Integer userID, String newPassword);

    boolean changeDepartment(Integer userID, Integer department);

    boolean deleteAccount(Integer userID);

    List<Priority> getPriorities();

    Priority createPriority(Priority priority);

    boolean changePriority(Priority priority);

    boolean deletePriority(Integer priorityID);
}
