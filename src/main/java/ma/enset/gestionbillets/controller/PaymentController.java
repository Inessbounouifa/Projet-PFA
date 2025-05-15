package ma.enset.gestionbillets.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import ma.enset.gestionbillets.entities.Event;
import ma.enset.gestionbillets.entities.QrCodeGenerator;
import ma.enset.gestionbillets.entities.PdfTicketGenerator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    // Injection de JavaMailSender pour l'envoi des emails
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
                                 @RequestParam String email, // Ajout du champ email
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
                    // 1. Génération QR Code
                    String qrContent = "TICKET-" + name + "-" + event.getId() + "-" + UUID.randomUUID();
                    String fileName = "qr-" + event.getId() + "-" + name + ".png";
                    String filePath = "C:/temp/" + fileName;

                    QrCodeGenerator.generateQrCodeToFile(qrContent, filePath);
                    System.out.println("✅ QR Code généré : " + filePath);

                    // 2. Génération du billet PDF
                    String pdfFileName = "billet-" + event.getId() + "-" + name + ".pdf";
                    String pdfFilePath = "C:/temp/" + pdfFileName;

                    PdfTicketGenerator.generateTicket(
                            pdfFilePath,
                            name,
                            filePath,
                            event.getTitle(),
                            event.getVenue(),
                            event.getDateTime().toString()
                    );
                    System.out.println("✅ Billet PDF généré : " + pdfFilePath);

                    // 3. Envoi de l’e-mail avec le billet PDF en pièce jointe
                    MimeMessage message = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(message, true);
                    helper.setTo(email);
                    helper.setSubject("🎫 Votre billet pour " + event.getTitle());
                    helper.setText("Bonjour " + name + ",\n\nVoici votre billet pour l'événement \"" + event.getTitle() + "\".\nVeuillez trouver votre billet ci-joint.\n\nMerci pour votre achat !");
                    helper.addAttachment(pdfFileName, new File(pdfFilePath));
                    mailSender.send(message);
                    System.out.println("📧 Email envoyé à : " + email);

                    lastPdfFileName = pdfFileName;

                } catch (Exception e) {
                    e.printStackTrace();
                    model.addAttribute("message", "Erreur pendant la génération ou l'envoi du billet.");
                    return "events/payment-error";
                }
            }

            session.removeAttribute("cart");

            model.addAttribute("message", "Paiement réussi via " + method + ", merci " + name + " !");
            model.addAttribute("pdfFileName", lastPdfFileName); // Lien ou iframe pour le PDF
            return "events/payment-success";

        } else {
            model.addAttribute("message", "Le panier est vide.");
            return "events/payment-error";
        }
    }
}
