package lkt.service;

import lkt.model.Priority;

import java.util.List;

public interface IAdminService {
    boolean changeUserPassword(Integer userID, String newPassword);

    boolean changeDepartment(Integer userID, Integer department);

    boolean deleteAccount(Integer userID);

    List<Priority> getPriorities();

    boolean changePriority(Integer priorityID, int levelOfPriority, String name, String timeToRespond, String timeToFinish);

    boolean deletePriority(Integer priorityID);
}
