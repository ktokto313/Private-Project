package repository;

import jakarta.data.repository.*;
import model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    @Find
    List<User> findByUsername(String username);

    @Find
    User findByUserID(Integer userID);

    @Save
    User upsert(User user);

    @Delete
    void delete(User user);
}
