package ma.enset.gestionbillets.repository;

import ma.enset.gestionbillets.entities.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {


}