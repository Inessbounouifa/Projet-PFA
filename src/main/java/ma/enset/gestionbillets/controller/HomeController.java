package ma.enset.gestionbillets.controller;

import lombok.AllArgsConstructor;
import ma.enset.gestionbillets.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class HomeController {

    private final EventRepository eventRepository;

    // ✅ ACCUEIL (accessible à tous)
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events/home";
    }

    // ✅ LISTE DES ÉVÉNEMENTS (accessible à tous)
    @GetMapping("/events/public")
    public String publicEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events/list_public"; // ⬅️ ici
    }
    @GetMapping("/events/view/{id}")
    public String viewEventDetails(@PathVariable Long id, Model model) {
        return eventRepository.findById(id)
                .map(event -> {
                    model.addAttribute("event", event);
                    return "events/details"; // ✔️ le fichier HTML que tu as déjà
                })
                .orElse("redirect:/events/public?error=notfound");
    }


}
