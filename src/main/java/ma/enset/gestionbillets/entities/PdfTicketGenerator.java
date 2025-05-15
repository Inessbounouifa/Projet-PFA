package ma.enset.gestionbillets.entities;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;

public class PdfTicketGenerator {

    public static void generateTicket(String filePath, String username, String qrCodePath, String eventTitle, String lieu, String date) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Titre
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
        Paragraph title = new Paragraph("🎫 Billet Électronique", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // Infos utilisateur & event
        document.add(new Paragraph("Nom du client : " + username));
        document.add(new Paragraph("Événement : " + eventTitle));
        document.add(new Paragraph("Lieu : " + lieu));
        document.add(new Paragraph("Date & Heure : " + date));
        document.add(Chunk.NEWLINE);

        // Image QR
        Image qrImage = Image.getInstance(qrCodePath);
        qrImage.scaleAbsolute(150, 150);
        qrImage.setAlignment(Image.ALIGN_CENTER);
        document.add(qrImage);

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Merci pour votre achat. À bientôt !"));

        document.close();
    }
}