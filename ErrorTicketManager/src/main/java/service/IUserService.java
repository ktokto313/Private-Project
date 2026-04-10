package service;

import model.User;

public interface IUserService {
    User addAccount(String username, String password);

    User getAccount(Integer id);

    User getAccountByUsername(String username);

    boolean changePassword(Integer userId, String password, String newPassword);
}
