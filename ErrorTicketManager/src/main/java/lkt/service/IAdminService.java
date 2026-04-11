package lkt.service;

public interface IAdminService {
    boolean changeUserPassword(Integer userID, String newPassword);

    boolean changeDepartment(Integer userID, Integer department);

    boolean deleteAccount(Integer userID);
}
