package org.blood_bank.repository;

import org.blood_bank.entity.Donor;
import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;
import org.blood_bank.util.JpaExecutor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JpaDonorRepository implements DonorRepository{

    @Override
    public Donor save(Donor donor) {
        return JpaExecutor.execute(entityManager -> {
            if (donor.getId() == null) {
                entityManager.persist(donor);
                return donor;
            }
            return entityManager.merge(donor);
        });
    }

    @Override
    public Optional<Donor> findById(Long id) {
        return Optional.ofNullable(JpaExecutor.execute(entityManager -> entityManager.find(Donor.class, id)));
    }

    @Override
    public Optional<Donor> findByCin(String cin) {
        return JpaExecutor.execute(entityManager -> {
            TypedQuery<Donor> query = entityManager.createQuery("SELECT d FROM Donor d WHERE d.cin = :cin", Donor.class);
            query.setParameter("cin", cin);
            List<Donor> result = query.getResultList();
            return result.stream().findFirst();
        });
    }

    @Override
    public List<Donor> findAll() {
        return JpaExecutor.execute(entityManager -> entityManager.createQuery("SELECT d FROM Donor d ORDER BY d.lastName, d.firstName", Donor.class).getResultList());
    }

    @Override
    public List<Donor> findByAvailability(DonorAvailabilityStatus status) {
        return JpaExecutor.execute(entityManager -> {
            TypedQuery<Donor> query = entityManager.createQuery("SELECT d FROM Donor d WHERE d.availabilityStatus = :status", Donor.class);
            query.setParameter("status", status);
            return query.getResultList();
        });
    }

    @Override
    public List<Donor> findCompatibleAvailableDonors(BloodGroup receiverGroup) {
        return JpaExecutor.execute(entityManager -> {
            TypedQuery<Donor> query = entityManager.createQuery(
                    "SELECT d FROM Donor d WHERE d.availabilityStatus = :status", Donor.class);
            query.setParameter("status", DonorAvailabilityStatus.AVAILABLE);
            List<Donor> donors = query.getResultList();
            return donors.stream()
                    .filter(donor -> donor.getBloodType().canDonateTo(receiverGroup))
                    .toList();
        });
    }

    @Override
    public void deleteById(Long id) {
        JpaExecutor.executeVoid(entityManager -> {
            Donor donor = entityManager.find(Donor.class, id);
            if (donor != null) {
                EntityManager managedEm = entityManager;
                managedEm.remove(donor);
            }
        });
    }
}
