package hrd.com.hrms.repository;

import hrd.com.hrms.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    <T> ScopedValue<T> findByUsernameOrEmail(@NotBlank(message = "Username or email is required") String usernameOrEmail, @NotBlank(message = "Username or email is required") String usernameOrEmail1);
}