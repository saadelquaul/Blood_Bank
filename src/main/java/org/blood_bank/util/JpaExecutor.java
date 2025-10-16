package org.blood_bank.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class JpaExecutor {

    private JpaExecutor() {}

    public static <T> T execute(Function<EntityManager, T> callback) {
        EntityManager entityManager = EntityManagerFactoryProvider.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            T result = callback.apply(entityManager);
            transaction.commit();
            return result;
        } catch (RuntimeException ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        } finally {
            if (entityManager.isOpen()) {
                EntityManagerFactoryProvider.close();

            }
        }


    }

    public static void executeVoid(Consumer<EntityManager> callback) {
        execute(entityManager -> {
            callback.accept(entityManager);
            return null;
        });
    }
}
