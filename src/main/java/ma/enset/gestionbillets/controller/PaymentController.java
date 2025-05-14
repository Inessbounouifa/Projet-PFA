package ma.enset.gestionbillets.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import ma.enset.gestionbillets.entities.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    @GetMapping
    public String showPaymentPage(HttpSession session, Model model) {
        List<Object[]> cart = (List<Object[]>) session.getAttribute("cart");
        double total = 0;
        if (cart != null) {
            for (Object[] item : cart) {
                Event event = (Event) item[0];
                int quantity = (int) item[1];
                total += event.getPrix() * quantity;
            }
        }
        model.addAttribute("total", total);
        return "events/payment";
    }

    @PostMapping("/confirm")
    public String processPayment(@RequestParam String name,
                                 @RequestParam String method,
                                 HttpSession session,
                                 Model model) {
        // Simule un paiement réussi
        session.removeAttribute("cart"); // Vide le panier
        model.addAttribute("message", "Paiement réussi via " + method + ", merci " + name + " !");
        return "events/payment-success";
    }
}
