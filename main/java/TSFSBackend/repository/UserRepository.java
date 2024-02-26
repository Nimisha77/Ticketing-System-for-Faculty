package TSFSBackend.repository;

import TSFSBackend.model.Ticket;
import TSFSBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findUserByGtUsername(String GT_username);
}
