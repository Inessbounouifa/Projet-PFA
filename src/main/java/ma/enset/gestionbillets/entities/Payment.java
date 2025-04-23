package ma.enset.gestionbillets.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private BigDecimal amount;

    private String paymentMethod; // (PayPal, Visa, etc.)

    private String paymentStatus; // (Success, Failed, Pending)
}