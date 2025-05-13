package ma.enset.gestionbillets.controller;


import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import ma.enset.gestionbillets.entities.Event;
import ma.enset.gestionbillets.repository.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final EventRepository eventRepository;

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") int quantity,
                            HttpSession session) {
        Event event = eventRepository.findById(id).orElseThrow();

        // panier : une liste d'objets personnalisés contenant event + quantité
        List<Object[]> cart = (List<Object[]>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        cart.add(new Object[]{event, quantity});
        session.setAttribute("cart", cart);

        return "redirect:/cart/view";
    }


    @GetMapping("/view")
    public String viewCart(HttpSession session, Model model) {
        List<Event> cart = (List<Event>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);
        return "events/view";

    }
    @GetMapping("/remove/{index}")
    public String removeFromCart(@PathVariable int index, HttpSession session) {
        List<Object[]> cart = (List<Object[]>) session.getAttribute("cart");
        if (cart != null && index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        session.setAttribute("cart", cart);
        return "redirect:/cart/view";
    }

}
