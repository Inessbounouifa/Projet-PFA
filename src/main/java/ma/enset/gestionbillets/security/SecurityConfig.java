package ma.enset.gestionbillets.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        PasswordEncoder encoder = passwordEncoder();
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(encoder.encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(encoder.encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(encoder.encode("1234")).roles("USER", "ADMIN").build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomAuthenticationSuccessHandler successHandler) throws Exception {
        http
                .formLogin(form -> form
                        .loginPage("/events/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        // ✅ VISITEURS
                        .requestMatchers(
                                "/", "/home",
                                "/events/public",
                                "/events/view/**",
                                "/events/search",
                                "/events/login",        // ✅ obligatoire pour que la page de login s'affiche
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()


                        // ✅ UTILISATEUR CONNECTÉ
                        .requestMatchers("/cart/**", "/payment/**").hasRole("USER")

                        // ✅ ADMINISTRATEUR
                        .requestMatchers("/events/admin/**").hasRole("ADMIN")

                        // ❗️ AUTRES = AUTH REQUISE
                        .anyRequest().authenticated()
                );

        return http.build();
    }




}
