package org.blood_bank.repository;

import org.blood_bank.entity.Donor;
import org.blood_bank.entity.enums.BloodGroup;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;

import java.util.List;
import java.util.Optional;

public interface DonorRepository {
    Donor save(Donor donor);

    Optional<Donor> findById(Long id);

    Optional<Donor> findByCin(String CIN);

    List<Donor> findAll();

    List<Donor> findByAvailability(DonorAvailabilityStatus status);

    List<Donor> findCompatibleAvailableDonors(BloodGroup bloodType);

    void deleteById(Long id);
}
