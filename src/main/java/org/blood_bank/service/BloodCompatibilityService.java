package org.blood_bank.service;

import org.blood_bank.entity.Donor;
import org.blood_bank.entity.Receiver;
import org.blood_bank.entity.enums.BloodGroup;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BloodCompatibilityService {

    public boolean isCompatible(BloodGroup donorGroup, BloodGroup receiverGroup) {
        Objects.requireNonNull(donorGroup, "donorGroup must not be null");
        Objects.requireNonNull(receiverGroup, "receiverGroup must not be null");
        return donorGroup.canDonateTo(receiverGroup);
    }

    public List<Donor> filterCompatibleDonors(Collection<Donor> donors, BloodGroup receiverGroup) {
        return donors.stream()
                .filter(donor -> donor.getBloodType() != null && isCompatible(donor.getBloodType(), receiverGroup))
                .collect(Collectors.toList());
    }

    public List<Receiver> filterCompatibleReceivers(Collection<Receiver> receivers, BloodGroup donorGroup) {
        return receivers.stream()
                .filter(receiver -> receiver.getBloodGroup() != null && isCompatible(donorGroup, receiver.getBloodGroup()))
                .collect(Collectors.toList());
    }
}
