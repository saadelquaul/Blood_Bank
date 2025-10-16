package org.blood_bank.service;

import org.blood_bank.entity.Donation;
import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.DonorAvailabilityStatus;
import org.blood_bank.entity.enums.ReceiverStatus;
import org.blood_bank.repository.DonorRepository;
import org.blood_bank.repository.JpaDonorRepository;
import org.blood_bank.repository.JpaReceiverRepository;
import org.blood_bank.repository.ReceiverRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AssignmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentService.class);
    private final DonorRepository donorRepository;
    private final ReceiverRepository receiverRepository;
    private final DonorService donorService;
    private final ReceiverService receiverService;
    private final BloodCompatibilityService compatibilityService;

    public AssignmentService() {
        this(new JpaDonorRepository(), new JpaReceiverRepository(), new DonorService(), new ReceiverService(), new BloodCompatibilityService());
    }

    public AssignmentService(DonorRepository donorRepository,
                             ReceiverRepository receiverRepository,
                             DonorService donorService,
                             ReceiverService receiverService,
                             BloodCompatibilityService compatibilityService) {
        this.donorRepository = donorRepository;
        this.receiverRepository = receiverRepository;
        this.donorService = donorService;
        this.receiverService = receiverService;
        this.compatibilityService = compatibilityService;
    }

    public AssignmentResult assign(Long donorId, Long receiverId) {
        Optional<Donor> donorOpt = donorRepository.findById(donorId);
        Optional<Receiver> receiverOpt = receiverRepository.findById(receiverId);
        if (donorOpt.isEmpty()) {
            return AssignmentResult.failure("Donor not found");
        }
        if (receiverOpt.isEmpty()) {
            return AssignmentResult.failure("Receiver not found");
        }

        Donor donor = donorOpt.get();
        Receiver receiver = receiverOpt.get();

        if (donor.getAvailabilityStatus() == DonorAvailabilityStatus.NOT_ELIGIBLE) {
            return AssignmentResult.failure("Donor is not eligible for donation");
        }
        if (donor.getAvailabilityStatus() == DonorAvailabilityStatus.NOT_AVAILABLE) {
            return AssignmentResult.failure("Donor is already assigned");
        }
        if (!compatibilityService.isCompatible(donor.getBloodType(), receiver.getBloodGroup())) {
            return AssignmentResult.failure("Incompatible blood groups");
        }
        if (receiver.getStatus() == ReceiverStatus.SATISFIED) {
            return AssignmentResult.failure("Receiver is already satisfied");
        }
        if (receiver.getRemainingUnits() <= 0) {
            return AssignmentResult.failure("Receiver does not need more units");
        }

        Donation donation = donorService.registerDonation(donor, receiver);
        donorService.markAsAssigned(donor, receiver);
        receiver.refreshStatus();
        receiverService.saveReceiver(receiver);

        LOGGER.info("Assigned donor {} to receiver {}", donorId, receiverId);
        return AssignmentResult.success(donation.getId());
    }

    public static class AssignmentResult {
        private final boolean success;
        private final String message;
        private final Long donationId;

        private AssignmentResult(boolean success, String message, Long donationId) {
            this.success = success;
            this.message = message;
            this.donationId = donationId;
        }

        public static AssignmentResult success(Long donationId) {
            return new AssignmentResult(true, "Assignment completed", donationId);
        }

        public static AssignmentResult failure(String message) {
            return new AssignmentResult(false, message, null);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Long getDonationId() {
            return donationId;
        }
    }
}
