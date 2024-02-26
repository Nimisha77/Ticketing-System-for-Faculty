package TSFSBackend.repository;

import TSFSBackend.model.Ticket;
import TSFSBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    Ticket findTicketById(int ticketid);
    List<Ticket> findTicketsByRelatedUsersOrderByCreatedDate(User user);
}