package ma.enset.gestionbillets.controller;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import ma.enset.gestionbillets.entities.Event;
import ma.enset.gestionbillets.entities.PdfTicketGenerator;
import ma.enset.gestionbillets.entities.QrCodeGenerator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/payment") // accessible aux users connectés
public class PaymentController {

    private final JavaMailSender mailSender;

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
                                 @RequestParam String email,
                                 @RequestParam String method,
                                 HttpSession session,
                                 Model model) {

        List<Object[]> cart = (List<Object[]>) session.getAttribute("cart");
        if (cart != null && !cart.isEmpty()) {
            String lastPdfFileName = null;

            for (Object[] item : cart) {
                Event event = (Event) item[0];
                int quantity = (int) item[1];

                try {
                    String qrContent = "TICKET-" + name + "-" + event.getId() + "-" + UUID.randomUUID();
                    String fileName = "qr-" + event.getId() + "-" + name + ".png";
                    String filePath = "C:/temp/" + fileName;

                    QrCodeGenerator.generateQrCodeToFile(qrContent, filePath);

                    String pdfFileName = "billet-" + event.getId() + "-" + name + ".pdf";
                    String pdfFilePath = "C:/temp/" + pdfFileName;

                    PdfTicketGenerator.generateTicket(
                            pdfFilePath, name, filePath,
                            event.getTitle(), event.getVenue(), event.getDateTime().toString()
                    );

                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(email);
                    helper.setSubject("🎫 Votre billet pour " + event.getTitle());
                    helper.setText("Bonjour " + name + ",\n\nVoici votre billet pour l'événement.");
                    helper.addAttachment(pdfFileName, new File(pdfFilePath));
                    mailSender.send(message);

                    lastPdfFileName = pdfFileName;

                } catch (Exception e) {
                    model.addAttribute("message", "Erreur pendant la génération ou l'envoi du billet.");
                    return "events/payment-error";
                }
            }

            session.removeAttribute("cart");
            model.addAttribute("message", "Paiement réussi via " + method + ", merci " + name + " !");
            model.addAttribute("pdfFileName", lastPdfFileName);
            return "events/payment-success";

        } else {
            model.addAttribute("message", "Le panier est vide.");
            return "events/payment-error";
        }
    }
}
