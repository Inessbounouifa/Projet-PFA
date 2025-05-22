package ma.enset.gestionbillets.controller;

import lombok.RequiredArgsConstructor;
import ma.enset.gestionbillets.entities.User;
import ma.enset.gestionbillets.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/add-admin")
    public String showAdminForm() {
        return "events/admin_add";
    }

    @PostMapping("/add-admin")
    public String addAdmin(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password) {
        User admin = new User();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole("ADMIN");
        userRepository.save(admin);
        return "redirect:/events/admin";
    }
}