package org.blood_bank.entity.enums;

public enum ReceiverUrgency {
    CRITICAL(4),
    URGENT(3),
    NORMAL(1);


    private final int requiredUnits;

    ReceiverUrgency(int requiredUnits) {
        this.requiredUnits = requiredUnits;
    }

    public int getRequiredUnits() {
        return requiredUnits;
    }

}
