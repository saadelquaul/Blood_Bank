package org.blood_bank.repository;

import org.blood_bank.entity.Donation;
import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.util.JpaExecutor;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
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
    public void deleteById(Long id) {
        JpaExecutor.executeVoid(entityManager -> {
            Donation donation = entityManager.find(Donation.class, id);
            if (donation != null) {
                entityManager.remove(donation);
            }
        });
    }

    @Override
    public Donation registerDonation(Long donorId, Long receiverId) {
        return JpaExecutor.execute(entityManager -> {
            Donor donor = entityManager.createQuery("SELECT d FROM Donor d LEFT JOIN FETCH d.donations WHERE d.id = :id", Donor.class)
                    .setParameter("id", donorId)
                    .getSingleResult();
            Receiver receiver = entityManager.createQuery("SELECT r FROM Receiver r LEFT JOIN FETCH r.donations WHERE r.id = :id", Receiver.class)
                    .setParameter("id", receiverId)
                    .getSingleResult();
            Donation donation = new Donation();
            donation.setDonor(donor);
            donation.setReceiver(receiver);
            donation.setDonationDate(LocalDateTime.now());
            entityManager.persist(donation);
            donor.getDonations().add(donation);
            receiver.getDonations().add(donation);
            return donation;
        });
    }
}

