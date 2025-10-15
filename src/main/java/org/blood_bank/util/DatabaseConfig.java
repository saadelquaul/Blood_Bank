package org.blood_bank.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("database.properties")) {

            if (input == null) {
                logger.error("Fichier database.properties introuvable");
                throw new RuntimeException("Impossible de trouver database.properties");
            }

            properties.load(input);
            logger.info("Configuration de la base de données chargée");

        } catch (IOException e) {
            logger.error("Erreur lors du chargement de database.properties", e);
            throw new RuntimeException("Erreur de chargement de la configuration", e);
        }
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUsername() {
        return properties.getProperty("db.username");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }
}
