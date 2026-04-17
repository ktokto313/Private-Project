package lkt.service;

import lkt.model.User;

public interface IUserService {
    User getAccountByUsername(String username);

    boolean changePassword(Integer userId, String password, String newPassword);
}
