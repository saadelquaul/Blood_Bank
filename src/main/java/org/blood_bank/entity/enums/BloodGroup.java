package org.blood_bank.entity.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.Map;
import java.util.EnumMap;
import java.util.Arrays;



public enum BloodGroup {
    O_NEGATIVE("O-"),
    O_POSITIVE("O+"),
    A_NEGATIVE("A-"),
    A_POSITIVE("A+"),
    B_NEGATIVE("B-"),
    B_POSITIVE("B+"),
    AB_NEGATIVE("AB-"),
    AB_POSITIVE("AB+");

    private static final Map<BloodGroup, Set<BloodGroup>> DONATION_MATRIX;

    static {
        Map<BloodGroup, Set<BloodGroup>> matrix = new EnumMap<>(BloodGroup.class);
        matrix.put(O_NEGATIVE, EnumSet.allOf(BloodGroup.class));
        matrix.put(O_POSITIVE, EnumSet.of(O_POSITIVE, A_POSITIVE, B_POSITIVE, AB_POSITIVE));
        matrix.put(A_NEGATIVE, EnumSet.of(A_NEGATIVE, A_POSITIVE, AB_POSITIVE, AB_NEGATIVE));
        matrix.put(A_POSITIVE, EnumSet.of(AB_POSITIVE, A_POSITIVE));
        matrix.put(B_NEGATIVE, EnumSet.of(B_NEGATIVE,B_POSITIVE,AB_NEGATIVE,AB_POSITIVE));
        matrix.put(B_POSITIVE, EnumSet.of(B_POSITIVE, AB_POSITIVE));
        matrix.put(AB_NEGATIVE, EnumSet.of(AB_POSITIVE, AB_NEGATIVE));
        matrix.put(AB_POSITIVE, EnumSet.of(AB_POSITIVE));
        DONATION_MATRIX = Collections.unmodifiableMap(matrix);
    }

    private final String label;

    BloodGroup(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean canDonateTo(BloodGroup receiverGroup) {
        return DONATION_MATRIX.getOrDefault(this, Collections.emptySet()).contains(receiverGroup);
    }

    public static BloodGroup fromLabel(String label) {
        return Arrays.stream(values()).filter(bloodGroup -> bloodGroup.label.equalsIgnoreCase(label))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Unknown blood group label: " + label));
    }

    @Override
    public String toString() {
        return label;
    }

}
