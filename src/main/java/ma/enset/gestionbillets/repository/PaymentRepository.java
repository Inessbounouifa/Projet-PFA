package ma.enset.gestionbillets.repository;

import ma.enset.gestionbillets.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}