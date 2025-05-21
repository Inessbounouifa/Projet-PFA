package ma.enset.gestionbillets.controller;

import lombok.AllArgsConstructor;
import ma.enset.gestionbillets.entities.Event;
import ma.enset.gestionbillets.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/events/admin") // restreint aux ADMIN dans SecurityConfig
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping
    public String getAllEvents(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("event", new Event());
        return "events/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Event> event = eventRepository.findById(id);
        event.ifPresent(e -> model.addAttribute("event", e));
        return event.isPresent() ? "events/form" : "redirect:/events/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/events/admin";
    }

    @PostMapping("/save")
    public String saveEvent(@ModelAttribute("event") Event event,
                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (!imageFile.isEmpty()) {
            String uploadDir = "src/main/resources/static/images/";
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = imageFile.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
                event.setImage(filename);
            }
        }

        eventRepository.save(event);
        return "redirect:/events/admin";
    }
    @GetMapping("/view/{id}")
    public String viewEventAdmin(@PathVariable Long id, Model model) {
        return eventRepository.findById(id)
                .map(event -> {
                    model.addAttribute("event", event);
                    return "events/details_admin"; // ✅ fichier HTML admin
                })
                .orElse("redirect:/events/admin?error=notfound");
    }

}
