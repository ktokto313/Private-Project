package lkt.repository;

import lkt.model.User;

public interface IUserRepository {
    User findByUsername(String username);

    User findByUserID(Integer userID);

    User insert(User user);

    User update(User user);

    boolean delete(User user);
}
