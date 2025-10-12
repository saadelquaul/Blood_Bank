package org.blood_bank.repository;

import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.ReceiverStatus;

import java.util.List;
import java.util.Optional;

public interface ReceiverRepository {

    Receiver save(Receiver receiver);

    Optional<Receiver> findById(Long id);

    Optional<Receiver> findByCin(String CIN);

    List<Receiver> findAllSortedByUrgency();

    List<Receiver> findByStatus(ReceiverStatus status);

    List<Receiver> findCompatibleReceivers(BloodGroup bloodType);

    void deleteById(Long id);
 }
