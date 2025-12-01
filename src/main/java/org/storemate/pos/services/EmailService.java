package org.storemate.pos.services;

import org.storemate.pos.models.EmailSettings;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Service class responsible for sending emails using SMTP.
 * <p>
 * This class supports sending HTML emails without attachments. It uses
 * {@link EmailSettings} to configure SMTP host, port, authentication, and SSL/TLS settings.
 * </p>
 * @author Jeet
 */
public class EmailService {

    private final EmailSettings settings;


    /**
     * Constructs an EmailService with the specified email settings.
     *
     * @param settings the {@link EmailSettings} containing SMTP host, port, credentials, and security configuration
     */
    public EmailService(EmailSettings settings) {
        this.settings = settings;
    }

    /**
     * Sends an HTML email to a specified recipient.
     * <p>
     * This method configures the SMTP session using the {@link EmailSettings} and sends an
     * HTML email. No attachments are supported. If sending fails, a {@link MessagingException}
     * will be thrown.
     * </p>
     *
     * @param toAddress the recipient email address
     * @param subject   the subject of the email
     * @param htmlBody  the HTML content of the email
     * @throws MessagingException if an error occurs while sending the email
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
