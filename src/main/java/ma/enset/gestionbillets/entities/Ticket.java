package ma.enset.gestionbillets.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event; // L'événement auquel ce billet appartient

    @ManyToOne
    private User user; // L'utilisateur qui a acheté ce billet

    private String qrCode; // QR Code associé au billet, peut être généré plus tard

    private LocalDateTime issueDate; // Date et heure d'émission du billet

    private boolean isValid; // Statut de validité du billet
}
