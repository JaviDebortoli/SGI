package SGI.repository;

import SGI.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUserName(String userName);
    boolean existsByUserNameAndIdUserNot(String userName, UUID idUser);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdUserNot(String email, UUID idUser);
    boolean existsByIdUserAndEnabledTrue(UUID idUser);
    Optional<User> findByIdUserAndEnabledTrue(UUID idUser);
    List<User> findByEnabledTrue();
}