package repository;

import jakarta.annotation.Nullable;
import model.User;
import org.hibernate.annotations.processing.Find;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    @Find @Nullable
    User findByUsername(String username);
}
