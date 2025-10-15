package org.blood_bank.repository;


import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.ReceiverStatus;
import org.blood_bank.entity.enums.ReceiverUrgency;
import org.blood_bank.util.JpaExecutor;

import javax.persistence.TypedQuery;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaReceiverRepository implements ReceiverRepository {

    @Override
    public Receiver save(Receiver receiver) {
        return JpaExecutor.execute(entityManager -> {
            if (receiver.getId() == null) {
                entityManager.persist(receiver);
                return receiver;
            }
            return entityManager.merge(receiver);
        });
    }

    @Override
    public Optional<Receiver> findById(Long id) {
        return Optional.ofNullable(JpaExecutor.execute(entityManager -> entityManager.find(Receiver.class, id)));
    }

    @Override
    public Optional<Receiver> findByCin(String cin) {
        return JpaExecutor.execute(entityManager -> {
            TypedQuery<Receiver> query = entityManager.createQuery("SELECT r FROM Receiver r WHERE r.cin = :cin", Receiver.class);            query.setParameter("cin", cin);
            List<Receiver> result = query.getResultList();
            return result.stream().findFirst();
        });
    }

    @Override
    public List<Receiver> findAllSortedByUrgency() {
        return JpaExecutor.execute(entityManager -> entityManager
                .createQuery("SELECT DISTINCT r FROM Receivers r LEFT JOIN FETCH r.donations d LEFT JOIN FETCH d.donors", Receiver.class)
                .getResultStream()
                .sorted(Comparator.comparing(Receiver::getUrgency, Comparator.comparingInt(ReceiverUrgency::getRequiredUnits)).reversed())
                .collect(Collectors.toList()));
    }

    @Override
    public List<Receiver> findByStatus(ReceiverStatus status) {
        return JpaExecutor.execute(entityManager -> {
            TypedQuery<Receiver> query = entityManager.createQuery("SELECT r FROM Receivers r WHERE r.status = :status", Receiver.class);
            query.setParameter("status", status);
            return query.getResultList();
        });
    }

    @Override
    public List<Receiver> findCompatibleReceivers(BloodGroup donorGroup) {
        return JpaExecutor.execute(entityManager -> entityManager
                .createQuery("SELECT r FROM Receivers r WHERE r.status = :status", Receiver.class)
                .setParameter("status", ReceiverStatus.PENDING)
                .getResultStream()
                .filter(receiver -> donorGroup.canDonateTo(receiver.getBloodGroup()))
                .collect(Collectors.toList()));
    }

    @Override
    public void deleteById(Long id) {
        JpaExecutor.executeVoid(entityManager -> {
            Receiver receiver = entityManager.find(Receiver.class, id);
            if (receiver != null) {
                entityManager.remove(receiver);
            }
        });
    }
}
