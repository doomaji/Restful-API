package springboot.security.jpa;

import org.springframework.data.repository.query.Param;
import springboot.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("id") Long id);
}
