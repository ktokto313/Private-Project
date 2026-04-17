package lkt.repository;

import lkt.model.Role;
import lkt.model.User;

import java.util.List;

public interface IUserRepository {
    User findByUsername(String username);

    User findByUserID(Integer userID);

    List<User> findUsersByRole(Role role);

    User insert(User user);

    User update(User user);

    boolean delete(User user);
}
