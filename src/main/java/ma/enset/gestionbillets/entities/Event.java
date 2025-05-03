package ma.enset.gestionbillets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
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
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name; // Nom de l'événement

    @NotNull
    private String venue; // Lieu de l'événement (remplacé 'location' par 'venue')

    private LocalDateTime dateTime; // Date et heure de l'événement

    @NotNull
    private int availableTickets;
    // Nombre de billets disponibles

    private double prix;

    private String image;
    private String title;         // <== ce champ est requis
    private String description;


    // Méthode pour réserver un billet et décrémenter le nombre de billets disponibles
    public void reserveTicket() {
        if (this.availableTickets > 0) {
            this.availableTickets--;
        } else {
            throw new RuntimeException("Aucun billet disponible pour cet événement.");
        }
    }

    public String getPrice() {
        return null;
    }

    public String getDate() {
        return null;
    }
}
