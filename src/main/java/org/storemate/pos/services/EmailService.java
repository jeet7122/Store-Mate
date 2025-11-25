package org.storemate.pos.services;

import org.storemate.pos.models.EmailSettings;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {

    private final EmailSettings settings;

    public EmailService(EmailSettings settings) {
        this.settings = settings;
    }

    /**
     * Sends HTML email (no attachments).
     * Throws MessagingException on failure.
     */
    public void sendHtmlEmail(String toAddress, String subject, String htmlBody) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", settings.getSmtpHost());
        props.put("mail.smtp.port", String.valueOf(settings.getSmtpPort()));
        props.put("mail.smtp.auth", String.valueOf(settings.isAuth()));

        if (settings.isSsl()) {
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            // Use STARTTLS if not using SSL
            props.put("mail.smtp.starttls.enable", "true");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(settings.getSmtpUser(), settings.getSmtpPassword());
            }
        });

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(settings.getFrom()));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress, false));
        msg.setSubject(subject);
        msg.setContent(htmlBody, "text/html; charset=utf-8");

        Transport.send(msg);
    }
}
