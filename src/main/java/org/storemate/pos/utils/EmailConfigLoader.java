package org.storemate.pos.utils;

import org.storemate.pos.models.EmailSettings;

import java.io.InputStream;
import java.util.Properties;

public class EmailConfigLoader {

    public static EmailSettings loadFromResources() {
        try (InputStream in = EmailConfigLoader.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (in == null) return null;
            Properties p = new Properties();
            p.load(in);

            EmailSettings s = new EmailSettings();
            s.setFrom(p.getProperty("mail.from"));
            s.setSmtpHost(p.getProperty("mail.smtp.host"));
            s.setSmtpPort(Integer.parseInt(p.getProperty("mail.smtp.port", "587")));
            s.setSmtpUser(p.getProperty("mail.smtp.user"));
            s.setSmtpPassword(p.getProperty("mail.smtp.password"));
            s.setSsl(Boolean.parseBoolean(p.getProperty("mail.smtp.ssl", "false")));
            s.setAuth(Boolean.parseBoolean(p.getProperty("mail.smtp.auth", "true")));
            return s;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
