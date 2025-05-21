package ma.enset.gestionbillets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/events/login")
    public String login() {
        return "events/login"; // correspond à src/main/resources/templates/events/login.html
    }
}
