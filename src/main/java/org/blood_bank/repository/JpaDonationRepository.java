package org.blood_bank.repository;

import org.blood_bank.entity.Donation;
import org.blood_bank.util.JpaExecutor;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JpaDonationRepository implements DonationRepository {

    @Override
    public Donation save(Donation donation) {
        return JpaExecutor.execute(entityManager -> {
            if (donation.getId() == null) {
                entityManager.persist(donation);
                return donation;
            }
            return entityManager.merge(donation);
        });
    }

    @Override
    public Optional<Donation> findById(Long id) {
        return Optional.ofNullable(JpaExecutor.execute(entityManager -> entityManager.find(Donation.class, id)));
    }

    @Override
    public List<Donation> findAll() {
        return JpaExecutor.execute(entityManager -> {
            TypedQuery<Donation> query = entityManager.createQuery("SELECT d FROM Donation d", Donation.class);
            return query.getResultList();
        });
    }

    @Override
    public void deleteDonation(Long id) {
        JpaExecutor.executeVoid(entityManager -> {
            Donation donation = entityManager.find(Donation.class, id);
            if (donation != null) {
                entityManager.remove(donation);
            }
        });
    }
}

