package ma.enset.gestionbillets.controller;

import lombok.RequiredArgsConstructor;
import ma.enset.gestionbillets.entities.User;
import ma.enset.gestionbillets.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showForm() {
        return "events/register"; // assure-toi que ce fichier existe bien dans templates/events/
    }

    @PostMapping("/register")
    public RedirectView register(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 🔐 encodage ici obligatoire
        user.setRole("USER");
        userRepository.save(user);
        return new RedirectView("/events/login");
    }

}
