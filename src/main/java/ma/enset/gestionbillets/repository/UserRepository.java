package ma.enset.gestionbillets.repository;

import ma.enset.gestionbillets.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long >{
    User findByUsername(String username);
}
