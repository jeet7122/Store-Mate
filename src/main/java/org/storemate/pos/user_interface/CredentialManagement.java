package org.storemate.pos.user_interface;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;




public class CredentialManagement {

    private static CredentialManagement instance;
    private final Properties credentials;
    private static final String CREDENTIALS_PATH = "resources/credentials.properties";


    private CredentialManagement() {
        credentials = new Properties();

        if (credentialsExist()) {
            try {
                loadCredentials();
                System.out.println("Credentials loaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to load credentials.");
            }
        } else {
            System.out.println("Credentials file does not exist. Setup required.");
        }
    }

    // Singleton use
    public static CredentialManagement getInstance() {
        if (instance == null) {
            instance = new CredentialManagement();
        }
        return instance;
    }

    // Check if credentials file exists
    public boolean credentialsExist() {
        return Files.exists(Paths.get(CREDENTIALS_PATH));
    }

    // Load credentials from file
    private void loadCredentials() throws IOException {
        try (InputStream input = new FileInputStream(CREDENTIALS_PATH)) {
            credentials.load(input);
        }
    }

    // Save credentials to file
    public void saveCredentials(String Url, String Name, String username, String password) throws IOException {
        credentials.setProperty("DB.URL", Url);
        credentials.setProperty("DB.NAME", Name);
        credentials.setProperty("DB.USERNAME", username);
        credentials.setProperty("DB.PASSWORD", password);

        // Ensure resources folder exists
        Path path = Paths.get("resources");
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        try (OutputStream output = new FileOutputStream(CREDENTIALS_PATH)) {
            credentials.store(output, "Database Credentials Saved");
        }
    }

    // Getters for the loaded credentials
    public String getDBUrl() {
        return credentials.getProperty("DB.URL");
    }

    public String getDBName() {
        return credentials.getProperty("DB.NAME");
    }

    public String getDBUsername() {
        return credentials.getProperty("DB.USERNAME");
    }

    public String getDBPassword() {
        return credentials.getProperty("DB.PASSWORD");
    }
}