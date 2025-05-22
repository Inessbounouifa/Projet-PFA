package ma.enset.gestionbillets.Config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import ma.enset.gestionbillets.entities.User;
import ma.enset.gestionbillets.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuperAdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("superadmin").isEmpty()) {
            User admin = new User();
            admin.setUsername("superadmin");
            admin.setEmail("admin@site.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }
    }
}