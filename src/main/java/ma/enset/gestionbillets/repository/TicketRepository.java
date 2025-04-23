package ma.enset.gestionbillets.repository;

import ma.enset.gestionbillets.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}