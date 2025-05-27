package ma.enset.gestionbillets.repository;

import ma.enset.gestionbillets.entities.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleKeyword, String descriptionKeyword);

    List<Event> findByTitleContainingIgnoreCase(String keyword);

    List<Event> findTop5ByOrderByDateTimeAsc();
}