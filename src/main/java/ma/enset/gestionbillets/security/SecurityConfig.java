package ma.enset.gestionbillets.security;

import lombok.RequiredArgsConstructor;
import ma.enset.gestionbillets.Services.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CustomAuthenticationSuccessHandler successHandler) throws Exception {
        http
                .userDetailsService(userDetailsService) // 🔧 obligatoire
                .formLogin(form -> form
                        .loginPage("/events/login")
                        .loginProcessingUrl("/events/login") // 👈 cette ligne manquait probablement
                        .successHandler(successHandler)
                        .permitAll()
                )

                .logout(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/home", "/register", "/events/register", "/events/public",
                                "/events/view/**", "/events/search", "/events/login",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()
                        .requestMatchers("/cart/**", "/payment/**").hasRole("USER")
                        .requestMatchers("/events/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()

                );

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

}
