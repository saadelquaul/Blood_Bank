package org.blood_bank.service;


import org.blood_bank.entity.enums.BloodGroup;


import java.util.Objects;

public class BloodCompatibilityService {

    public boolean isCompatible(BloodGroup donorGroup, BloodGroup receiverGroup) {
        Objects.requireNonNull(donorGroup, "donorGroup must not be null");
        Objects.requireNonNull(receiverGroup, "receiverGroup must not be null");
        return donorGroup.canDonateTo(receiverGroup);
    }
}
