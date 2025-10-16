package org.blood_bank.service;

import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;
import org.blood_bank.repository.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReceiverService {

    private final ReceiverRepository receiverRepository;
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;

    public ReceiverService() {
        this(new JpaReceiverRepository(), new JpaDonorRepository(), new JpaDonationRepository());
    }

    public ReceiverService(ReceiverRepository receiverRepository,
                           DonorRepository donorRepository,
                           DonationRepository donationRepository) {
        this.receiverRepository = receiverRepository;
        this.donorRepository = donorRepository;
        this.donationRepository = donationRepository;
    }

    public List<String> validateReceiver(Receiver receiver) {
        List<String> errors = new ArrayList<>();
        if (receiver.getFirstName() == null || receiver.getFirstName().isBlank()) {
            errors.add("First name is required");
        }
        if (receiver.getLastName() == null || receiver.getLastName().isBlank()) {
            errors.add("Last name is required");
        }
        if (receiver.getCin() == null || receiver.getCin().isBlank()) {
            errors.add("CIN is required");
        }
        if (receiver.getPhone() == null || receiver.getPhone().isBlank()) {
            errors.add("Phone is required");
        }
        if (receiver.getDateOfBirth() == null) {
            errors.add("Date of birth is required");
        }
        if (receiver.getBloodGroup() == null) {
            errors.add("Blood group is required");
        }
        if (receiver.getGender() == null) {
            errors.add("Gender is required");
        }
        if (receiver.getUrgency() == null) {
            errors.add("Urgency is required");
        }
        if (receiver.getDateOfBirth() != null) {
            int age = Period.between(receiver.getDateOfBirth(), LocalDate.now()).getYears();
            if (age < 0 || age > 120) {
                errors.add("Invalid birth date");
            }
        }
        return errors;
    }

    public Receiver saveReceiver(Receiver receiver) {
        receiver.refreshStatus();
        return receiverRepository.save(receiver);
    }

    public List<Receiver> findAllSorted() {
        return receiverRepository.findAllSortedByUrgency();
    }

    public Optional<Receiver> findById(Long id) {
        if (id == null) {
        throw new IllegalArgumentException("Receiver ID cannot be null");
        
    }
        return receiverRepository.findById(id);
    }

    public Optional<Receiver> findByCin(String cin) {
        return receiverRepository.findByCin(cin);
    }

    public void deleteReceiver(Long id) {
        receiverRepository.findById(id).ifPresent(receiver -> {
            receiver.getDonations().forEach(donation -> {
                Donor donor = donation.getDonor();
                donor.setCurrentReceiver(null);
                donor.setAvailabilityStatus(DonorAvailabilityStatus.AVAILABLE);
                donorRepository.save(donor);
                donationRepository.deleteById(donation.getId());
            });
            receiverRepository.deleteById(id);
        });
    }


}

