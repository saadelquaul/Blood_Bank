package org.blood_bank.util;

import io.github.cdimascio.dotenv.Dotenv;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public final class EntityManagerFactoryProvider {
    private static volatile EntityManagerFactory entityManagerFactory;

    private EntityManagerFactoryProvider() {

    }

    public static void initialize(String persistenceUnitName) {
        if (entityManagerFactory == null) {

            Map<String, String> properties = new HashMap<>();
            properties.put("javax.persistence.jdbc.url", DatabaseConfig.getUrl());
            properties.put("javax.persistence.jdbc.user", DatabaseConfig.getUsername());
            properties.put("javax.persistence.jdbc.password", DatabaseConfig.getPassword());

            entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, properties);

        }
    }

    public static EntityManager getEntityManager() {
        if(entityManagerFactory == null) {
            initialize("bankBloodPU");
        }
        return entityManagerFactory.createEntityManager();
    }

    public static void close() {
        if(entityManagerFactory != null && entityManagerFactory.isOpen()) {

            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }

    private static Map<String, Object> loadOverrides() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        Map<String, Object> overrides = new HashMap<>();
        applyOverride(overrides, "javax.persistence.jdbc.url", resolve("DB_URL", dotenv));
        applyOverride(overrides, "javax.persistence.jdbc.user", resolve("DB_USER", dotenv));
        applyOverride(overrides, "javax.persistence.jdbc.password", resolve("DB_PASSWORD", dotenv));
//        applyOverride(overrides, "javax.persistence.jdbc.driver", resolve("DB_DRIVER", dotenv));
        return overrides;
    }

    private static void applyOverride(Map<String, Object> overrides, String propertyKey, Optional<String> value) {
        value.filter(v -> !v.isBlank())
                .ifPresent(v -> overrides.put(propertyKey, v));
    }

    private static Optional<String> resolve(String key, Dotenv dotenv) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.isBlank()) {
            return Optional.of(envValue);
        }
        String dotenvValue = dotenv.get(key);
        return Optional.ofNullable(dotenvValue);
    }


}
