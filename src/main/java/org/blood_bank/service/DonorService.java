package org.blood_bank.service;

import org.blood_bank.entity.Donation;
import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;
import org.blood_bank.entity.enums.MedicalFlags;
import org.blood_bank.repository.*;
import java.time.LocalDate;
import java.util.*;

public class DonorService {


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
        if(donor.getBloodType() == null) {
            errors.add("Blood group is required");
        }
        if(donor.getGender() == null ) {
            errors.add ("Gender is required");
        }


        return errors;
    }
    public boolean isDonorEligible(Donor donor) {
            // Check medical flags
            if (donor.getMedicalFlags() != null &&
                    donor.getMedicalFlags().stream().anyMatch(DISQUALIFYING_FLAGS::contains)) {
                donor.setAvailabilityStatus(DonorAvailabilityStatus.NOT_ELIGIBLE);
                return false;
            }

            // Check weight
            if (donor.getWeight() != null && donor.getWeight() < 50) {
                donor.setAvailabilityStatus(DonorAvailabilityStatus.NOT_ELIGIBLE);
                return false;
            }

            // Check date of birth (example: must be at least 18 years old)
            if (donor.getDateOfBirth() != null &&
                    donor.getDateOfBirth().isAfter(LocalDate.now().minusYears(18))) {
                donor.setAvailabilityStatus(DonorAvailabilityStatus.NOT_ELIGIBLE);
                return false;
            }

            return true;
        }



    public Donor saveDonor(Donor donor) {

        if(!isDonorEligible(donor)) {
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

    public void markAsAssigned(Donor donor, Receiver receiver) {
        donor.setCurrentReceiver(receiver);
        donor.setAvailabilityStatus(DonorAvailabilityStatus.NOT_AVAILABLE);
        donor.setLastDonationDate(LocalDate.now());
        donorRepository.save(donor);
    }

    public Donation registerDonation(Donor donor, Receiver receiver) {
        Donation donation = donationRepository.registerDonation(donor.getId(), receiver.getId());
        donorRepository.findById(donor.getId()).ifPresent(donor1 -> {
            donor.setLastDonationDate(LocalDate.now());
            if (donor.getCurrentReceiver() != null && donor.getCurrentReceiver().getId().equals(receiver.getId())) {
                donor.setCurrentReceiver(null);
                donor.setAvailabilityStatus(DonorAvailabilityStatus.AVAILABLE);
            }
            donor.setLastDonationDate(LocalDate.now());
            donorRepository.save(donor);
        });
        return donation;
    }

}
