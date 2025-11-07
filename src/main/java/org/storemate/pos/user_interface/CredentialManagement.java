package org.storemate.pos.user_interface;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;




public class CredentialManagement {

    private static CredentialManagement instance;
    private final Properties credentials;
    private static final String CREDENTIALS_PATH = "src/main/resources/credential.properties";


    private CredentialManagement() {
        credentials = new Properties();

        if (credentialsExist()) {
            try {
                loadCredentials();
                System.out.println("Credentials loaded successfully.");
            } catch (IOException e) {
                System.out.println("IO: " + e.getMessage());
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
    public void saveCredentials(String Url, String Name, String username, String password, String databaseProvider) throws IOException {
        String jdbcUrl = "";
        switch (databaseProvider){
            case "Postgres": jdbcUrl = "jdbc:postgresql://" + Url;break;
            case "MySql", "College Server": jdbcUrl = "jdbc:mysql://" + Url; break;
            case "MariaDB": jdbcUrl = "jdbc:mariadb://" + Url; break;
            case null, default:
                System.out.println("Invalid value");
        }
        credentials.setProperty("DB.URL", jdbcUrl);
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