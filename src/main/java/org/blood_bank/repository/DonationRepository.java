package org.blood_bank.repository;

import org.blood_bank.entity.Donation;

import java.util.List;
import java.util.Optional;

public interface DonationRepository {

    Donation save(Donation donation);

    Optional<Donation> findById(Long id);

    List<Donation> findAll();

    void deleteById(Long id);
}
