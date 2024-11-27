package com.example.pfa.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

    public void envoyerEmailConfirmationDemande(String email, String nomClient, String numeroDemande) throws MessagingException {
        String subject = "Confirmation de votre demande de crédit";
        String content = createConfirmationEmailTemplate(nomClient, numeroDemande);
        sendEmail(email, subject, content);
    }

    public void envoyerEmailStatusDemande(String email, String nomClient, String status, String numeroDemande) throws MessagingException {
        String subject = "Mise à jour de votre demande de crédit";
        String content = createStatusUpdateEmailTemplate(nomClient, status, numeroDemande);
        sendEmail(email, subject, content);
    }

    public void envoyerEmailDocumentsManquants(String email, String nomClient, String numeroDemande, String[] documentsManquants) throws MessagingException {
        String subject = "Documents manquants pour votre demande de crédit";
        String content = createMissingDocumentsEmailTemplate(nomClient, numeroDemande, documentsManquants);
        sendEmail(email, subject, content);
    }

    private String createConfirmationEmailTemplate(String nomClient, String numeroDemande) {
        return String.format("""
            <html>
                <body style='font-family: Arial, sans-serif;'>
                    <h2>Confirmation de demande de crédit</h2>
                    <p>Cher/Chère %s,</p>
                    <p>Nous avons bien reçu votre demande de crédit numéro <strong>%s</strong>.</p>
                    <p>Notre équipe va étudier votre dossier dans les plus brefs délais.</p>
                    <p>Vous recevrez une notification dès que votre demande sera traitée.</p>
                    <p>Cordialement,<br>L'équipe du service crédit</p>
                </body>
            </html>
            """, nomClient, numeroDemande);
    }

    private String createStatusUpdateEmailTemplate(String nomClient, String status, String numeroDemande) {
        return String.format("""
            <html>
                <body style='font-family: Arial, sans-serif;'>
                    <h2>Mise à jour de votre demande de crédit</h2>
                    <p>Cher/Chère %s,</p>
                    <p>Le statut de votre demande de crédit numéro <strong>%s</strong> a été mis à jour.</p>
                    <p>Nouveau statut : <strong>%s</strong></p>
                    <p>Connectez-vous à votre espace personnel pour plus de détails.</p>
                    <p>Cordialement,<br>L'équipe du service crédit</p>
                </body>
            </html>
            """, nomClient, numeroDemande, status);
    }

    private String createMissingDocumentsEmailTemplate(String nomClient, String numeroDemande, String[] documentsManquants) {
        StringBuilder documentsHtml = new StringBuilder("<ul>");
        for (String doc : documentsManquants) {
            documentsHtml.append("<li>").append(doc).append("</li>");
        }
        documentsHtml.append("</ul>");

        return String.format("""
            <html>
                <body style='font-family: Arial, sans-serif;'>
                    <h2>Documents manquants</h2>
                    <p>Cher/Chère %s,</p>
                    <p>Pour compléter votre demande de crédit numéro <strong>%s</strong>, nous avons besoin des documents suivants :</p>
                    %s
                    <p>Merci de nous faire parvenir ces documents dès que possible.</p>
                    <p>Cordialement,<br>L'équipe du service crédit</p>
                </body>
            </html>
            """, nomClient, numeroDemande, documentsHtml.toString());
    }
    public void envoyerNotificationDemande(String email, String subject, String content) throws MessagingException {
        sendEmail(email, subject, content);
    }

}