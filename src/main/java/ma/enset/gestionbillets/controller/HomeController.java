package ma.enset.gestionbillets.controller;
import lombok.AllArgsConstructor;
import ma.enset.gestionbillets.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {

    private final EventRepository eventRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events/home";// ==> resources/templates/home.html
    }
}

