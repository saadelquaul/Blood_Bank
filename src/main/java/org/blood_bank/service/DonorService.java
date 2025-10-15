package org.blood_bank.service;

import org.blood_bank.entity.Donation;
import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;
import org.blood_bank.entity.enums.MedicalFlags;
import org.blood_bank.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DonorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DonorService.class);
    private static final Set<MedicalFlags> DISQUALIFYING_FLAGS = EnumSet.of(
            MedicalFlags.HEPATITIS,
            MedicalFlags.HIV,
            MedicalFlags.DIABETES,
            MedicalFlags.BREASTFEEDING,
            MedicalFlags.PREGNANCY
    );

    private final DonorRepository donorRepository;
    private final ReceiverRepository receiverRepository;
    private final DonationRepository donationRepository;

    public DonorService() {
        this(new JpaDonorRepository(), new JpaReceiverRepository(), new JpaDonationRepository());

    }

    public DonorService(DonorRepository donorRepository, ReceiverRepository receiverRepository, DonationRepository donationRepository) {
        this.donorRepository = donorRepository;
        this.receiverRepository = receiverRepository;
        this.donationRepository = donationRepository;
    }

    public List<String> validateDonor(Donor donor) {
        List<String> errors = new ArrayList<>();
        if(donor.getFirstName() == null || donor.getFirstName().isBlank()) {
            errors.add("first name is required");
        }
        if(donor.getLastName() == null || donor.getLastName().isBlank()) {
            errors.add("last name is required");
        }
        if(donor.getCin() == null || donor.getCin().isBlank()) {
            errors.add("CIN is required");
        }
        if(donor.getPhone() == null || donor.getPhone().isBlank()) {
            errors.add("Phone number is required");
        }
        if(donor.getDateOfBirth() == null) {
            errors.add("Date of birth is required");
        }
        if(donor.getWeight() == null || donor.getWeight() <= 0) {
            errors.add("Weight must be a positive Number greater than 0");
        }
        if(donor.getBloodType() == null) {
            errors.add("Blood type/group is required");
        }
        if(donor.getGender() == null ) {
            errors.add ("Gender is required");
        }

        if(donor.getDateOfBirth() != null) {
            int age = donor.getAge();
            if(age < 18 || age > 65 ) {
                errors.add("Donor age must be between 18 and 65");
            }
        }

        if(donor.getWeight() != null && donor.getWeight() < 50 ) {
            errors.add("weight must be at least 50 kg");
        }

        if(donor.getMedicalFlags() != null) {
            donor.getMedicalFlags().stream()
                    .filter(DISQUALIFYING_FLAGS::contains)
                    .findFirst()
                    .ifPresent(flag -> errors.add("Donor is not eligible due to medical flag: " + flag.name()));
        }
        return errors;
    }

    public Donor saveDonor(Donor donor) {
        List<String> errors = validateDonor(donor);
        if(!errors.isEmpty()) {
            donor.setAvailabilityStatus(DonorAvailabilityStatus.NOT_ELIGIBLE);
        }else if (donor.getCurrentReceiver() != null) {
            donor.setAvailabilityStatus(DonorAvailabilityStatus.NOT_AVAILABLE);
        } else {
            donor.setAvailabilityStatus(DonorAvailabilityStatus.AVAILABLE);
        }

        return donorRepository.save(donor);
    }

    public Optional<Donor> findById(Long id) {
        return donorRepository.findById(id);
    }

    public Optional<Donor> findByCin(String cin) {
        return donorRepository.findByCin(cin);
    }

    public List<Donor> findALl() {
        return donorRepository.findAll();
    }

    public void deleteDonor(Long id){
        donorRepository.findById(id)
                .ifPresent(donor -> {
                    donor.getDonations().forEach(donation -> {
                        Receiver receiver = donation.getReceiver();
                        if(receiver != null) {
                            receiver.getDonations().remove(donation);
                            receiver.refreshStatus();
                            receiverRepository.save(receiver);
                        }
                        donationRepository.deleteById(donation.getId());
                    });
                    Optional.ofNullable(donor.getCurrentReceiver()).ifPresent(receiver -> {
                        receiver.getDonations().removeIf(donation -> donation.getDonor().equals(donor));
                        receiver.refreshStatus();
                        receiverRepository.save(receiver);
                    });
                    donorRepository.deleteById(id);
                });
    }

    public List<Receiver> findCompatibleReceiverForDonor(Long id) {
            return donorRepository.findById(id)
                    .map(donor -> receiverRepository.findCompatibleReceivers(donor.getBloodType()))
                    .orElseGet(List::of);
    }

    public void markAsAssigned(Donor donor, Receiver receiver) {
        donor.setCurrentReceiver(receiver);
        donor.setAvailabilityStatus(DonorAvailabilityStatus.NOT_AVAILABLE);
        donor.setLastDonationDate(LocalDate.now());
        donorRepository.save(donor);
    }

    public void markAsAvailable(Donor donor) {
        donor.setCurrentReceiver(null);
        donor.setAvailabilityStatus(DonorAvailabilityStatus.AVAILABLE);
        donorRepository.save(donor);
    }

    public Donation registerDonation(Donor donor, Receiver receiver) {
            Donation donation = new Donation();
            donation.setDonor(donor);
            donation.setReceiver(receiver);
            donation.setDonationDate(LocalDateTime.now());
            donationRepository.save(donation);
            LOGGER.info("Donation registered: donor {} -> receiver {}", donor.getId(), receiver.getId());
            donor.getDonations().add(donation);
            receiver.getDonations().add(donation);

            return donation;
    }

}
